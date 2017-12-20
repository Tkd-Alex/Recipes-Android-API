package com.github.raa.recipes.android.api;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.declarations.ReferenceTypeDeclaration;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import com.github.javaparser.ast.body.MethodDeclaration;

public class Analyzer implements CommitVisitor {

	private static void showReferenceTypeDeclaration(ReferenceTypeDeclaration rtd) {
		System.out.println(String.format("=== %s ===",  rtd.getQualifiedName()));
		System.out.println(" fields: ");
		rtd.getAllFields().forEach(f -> 
					System.out.println(String.format("\t %s %s", f.getType(), f.getName()))
				);
		System.out.println(" methods:");
		rtd.getAllMethods().forEach(m -> 
					System.out.println(String.format("\t %s ", m.getQualifiedSignature()))
				);
	}
	
	@Override
	public void process(SCMRepository repo, Commit commit, PersistenceMechanism writer) {
		for(Modification m: commit.getModifications()) {
			CompilationUnit cu = JavaParser.parse(m.getSourceCode());
			List<MethodCallExpr> methodCalls = Navigator.findAllNodesOfGivenClass(cu, MethodCallExpr.class);
			List<AssignExpr> assignExpr = Navigator.findAllNodesOfGivenClass(cu,  AssignExpr.class);
			List<FieldDeclaration> fieldDeclaration = Navigator.findAllNodesOfGivenClass(cu, FieldDeclaration.class);
			List<Comment> comments = cu.getAllContainedComments();
			try {
				final TypeSolver typeSolver = JarTypeSolver.getJarTypeSolver("/home/alessandro/Android/Sdk/platforms/android-23/android.jar");
				
				methodCalls.forEach(em -> System.out.println(
						"==============" + 
						"\nCommit: " + commit.getHash() + 
						"\nMsg: " + commit.getMsg() + 
						"\nFile: " + m.getFileName() +
						"\nName method: " + em.getName() + 
						"\nArguments: " + em.getArguments() +
						"\nSolver: " +
						JavaParserFacade.get(typeSolver).solve(em).getCorrespondingDeclaration().getQualifiedSignature()
						));
				
				assignExpr.forEach(as ->  System.out.println(
						"**************" + 
						"\nAssegnazione: " + as.toString() + 
						"\nSolver: " + JavaParserFacade.get(typeSolver).getType(as).describe() +
						"\nSolver: " + JavaParserFacade.get(typeSolver).getType(as)
						));
				
				fieldDeclaration.forEach(dec -> System.out.println(
						"++++++++++++" +
						"\nDeclaration: " + dec.toString() +
						"\nVariabile:" + dec.getVariables()
						));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Eccezione generata: " + e);
				e.printStackTrace();
			}
			
			//showReferenceTypeDeclaration(typeSolver.solveType("java.lang.Object"));
			//showReferenceTypeDeclaration(typeSolver.solveType("java.lang.String"));
			//showReferenceTypeDeclaration(typeSolver.solveType("java.util.List"));
			
			//Optional<ClassOrInterfaceDeclaration> classStore = cu.getClassByName("AppStoreApp");
			//List<MethodDeclaration> methods = classStore.get().getMethods();
			/*
			for(MethodDeclaration md : methods) {
					System.out.println("=============");
					System.out.println("Commit hash: " + commit.getHash());
					System.out.println("[" + md.getBegin().get().line + "] \n" + md);
					System.out.println("Name as string: " + md.getNameAsString());
					System.out.println("Declaration: " + md.getDeclarationAsString());
					System.out.println("Params: " + md.getParameters());
					System.out.println("Body: " + md.getBody());
				}
			}
			/*
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
