package com.github.raa.recipes.android.api;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.GitRemoteRepository;

public class Main implements Study {
    public static void main(String[] args) {
    	new RepoDriller().start(new Main());
    }

	@Override
	public void execute() {
		CSVFile csv = new CSVFile("/home/alessandro/Documenti/test.csv");
		Analyzer analyzer = new Analyzer();
		String url = "https://github.com/d4rken/reddit-android-appstore";
		new RepositoryMining()
			.in(GitRemoteRepository.hostedOn(url)
					.inTempDir("/tmp/repodriller")
					.buildAsSCMRepository())
			.through(Commits.all())
			.process(analyzer, csv)
			.mine();
		
	}
}
