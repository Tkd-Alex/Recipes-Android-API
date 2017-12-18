package com.github.raa.recipes.android.api;

import java.util.List;
import java.util.Optional;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.visitor.VoidVisitor;

public class Analyzer implements CommitVisitor {

	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		for(Modification m: commit.getModifications()) {
			CompilationUnit cu = JavaParser.parse(m.getSourceCode());
			Optional<ClassOrInterfaceDeclaration> classStore = cu.getClassByName("AppStoreApp");
			VoidVisitor<?> methodNameVisitor = new MethodNamePrinter();
			methodNameVisitor.visit(cu,  null);
			if(classStore.isPresent()) { 
				//System.out.println(classStore.toString());
				
			}
			/*
			List<Comment> comments = cu.getAllContainedComments();
			for(Comment c: comments) {
				writer.write(
						commit.getHash(), 
						m.getFileName(), 
						c.toString().replaceAll("\n", "")
					);
			}
			*/
		}
	}

	@Override
	public String name(){ 
		return "test"; 
	}
	
}
