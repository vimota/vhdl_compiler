package ece351.vhdl;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.parboiled.common.ImmutableList;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.EqualExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NAndExpr;
import ece351.common.ast.NOrExpr;
import ece351.common.ast.NaryAndExpr;
import ece351.common.ast.NaryOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.common.ast.XNOrExpr;
import ece351.common.ast.XOrExpr;
import ece351.common.visitor.PostOrderExprVisitor;
import ece351.util.CommandLine;
import ece351.vhdl.ast.Architecture;
import ece351.vhdl.ast.Component;
import ece351.vhdl.ast.DesignUnit;
import ece351.vhdl.ast.IfElseStatement;
import ece351.vhdl.ast.Process;
import ece351.vhdl.ast.Statement;
import ece351.vhdl.ast.VProgram;

/**
 * Inlines logic in components to architecture body.
 */
public final class Elaborator extends PostOrderExprVisitor {

	private final Map<String, String> current_map = new LinkedHashMap<String, String>();
	
	public static void main(String[] args) {
		System.out.println(elaborate(args));
	}
	
	public static VProgram elaborate(final String[] args) {
		return elaborate(new CommandLine(args));
	}
	
	public static VProgram elaborate(final CommandLine c) {
        final VProgram program = DeSugarer.desugar(c);
        return elaborate(program);
	}
	
	public static VProgram elaborate(final VProgram program) {
		final Elaborator e = new Elaborator();
		return e.elaborateit(program);
	}

	private VProgram elaborateit(final VProgram root) {
			// In the elaborator, an architecture's list of signals, and set of statements may change (grow)
						//populate dictionary/map	
						//add input signals, map to ports
						//add output signals, map to ports
						//add local signals, add to signal list of i						
						//loop through the statements in the architecture body		
							//make the appropriate variable substitutions for signal assignment statements
							//make the appropriate variable substitutions for processes (sensitivity list, if/else body statements)
// TODO: 58 lines snipped
throw new ece351.util.Todo351Exception();
	}
	
	// you do not have to use these helper methods; we found them useful though
	private Process expandProcessComponent(final Process process) {
// TODO: 15 lines snipped
throw new ece351.util.Todo351Exception();
	}
	
	// you do not have to use these helper methods; we found them useful though
	private  IfElseStatement changeIfVars(final IfElseStatement s) {
// TODO: 14 lines snipped
throw new ece351.util.Todo351Exception();
	}

	// you do not have to use these helper methods; we found them useful though
	private AssignmentStatement changeStatementVars(final AssignmentStatement s){
// TODO: 2 lines snipped
throw new ece351.util.Todo351Exception();
	}
	
	
	@Override
	public Expr visitVar(VarExpr e) {
		// TODO replace/substitute the variable found in the map
// TODO: 1 lines snipped
throw new ece351.util.Todo351Exception();
	}
	
	// do not rewrite these parts of the AST
	@Override public Expr visitConstant(ConstantExpr e) { return e; }
	@Override public Expr visitNot(NotExpr e) { return e; }
	@Override public Expr visitAnd(AndExpr e) { return e; }
	@Override public Expr visitOr(OrExpr e) { return e; }
	@Override public Expr visitXOr(XOrExpr e) { return e; }
	@Override public Expr visitEqual(EqualExpr e) { return e; }
	@Override public Expr visitNAnd(NAndExpr e) { return e; }
	@Override public Expr visitNOr(NOrExpr e) { return e; }
	@Override public Expr visitXNOr(XNOrExpr e) { return e; }
	@Override public Expr visitNaryAnd(NaryAndExpr e) { return e; }
	@Override public Expr visitNaryOr(NaryOrExpr e) { return e; }
}
