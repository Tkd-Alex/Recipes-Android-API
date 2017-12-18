package com.github.raa.recipes.android.api;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

public class Analyzer implements CommitVisitor {

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		for(Modification m: commit.getModifications()) {
			String []lines = m.getDiff().split("\n");
			for(String line: lines) {
				System.out.println(commit.getHash());
				System.out.println(line);
				writer.write(commit.getHash(), line);
			}
		}
	}

	@Override
	public String name(){ 
		return "test"; 
	}
	
}
