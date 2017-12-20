package com.github.raa.recipes.android.api;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodNamePrinter extends VoidVisitorAdapter<Object> {
	@Override
	public void visit(MethodCallExpr md, Object arg) {
		super.visit(md, arg);
		System.out.println(md.getName());
		//System.out.println("[" + md.getBegin().get().line + "]" + md.getName());
	}
}