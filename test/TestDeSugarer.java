package ece351.vhdl.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import ece351.common.ast.AndExpr;
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
import ece351.util.BaseTest351;
import ece351.util.CommandLine;
import ece351.util.ExaminableProperties;
import ece351.util.TestInputs351;
import ece351.vhdl.DeSugarer;
import ece351.vhdl.VParser;
import ece351.vhdl.ast.VProgram;


@RunWith(Parameterized.class)
public final class TestDeSugarer extends BaseTest351 {

	private final File f;

	public TestDeSugarer(final File f) {
		this.f = f;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> files() {
		return TestInputs351.vhdlFiles();
	}

	@Test
	public void desugar() {
		final String inputSpec = f.getAbsolutePath();
		final CommandLine c = new CommandLine(inputSpec);
		System.out.println("processing " + inputSpec);
		// parse from the file to construct first AST
		final VProgram vp1 = DeSugarer.desugar(c);
		assertTrue(vp1.repOk());
		// pretty-print the first AST
		final String pp = vp1.toString();
		System.out.println("desugared program: ");
		System.out.println(pp);

		// check the desugared program does not contain any exotic operators
		final DesugarCheck dsc = new DesugarCheck();
		// this will thrown an exception if the tree is not desugared
		dsc.traverseVProgram(vp1);
		
		// find the appropriate solution file for comparison
		String solnSpec = "";
		for (final Object[] obj : TestInputs351.desugaredVhdlFiles()) {
			if (obj[0] instanceof File) {
				final File soln = (File)obj[0];
				if (f.getName().equals(soln.getName())) {
					solnSpec = soln.getAbsolutePath();
					break;
				}
			}
		}
		
		assertTrue("no matching file found to compare the input file: " + inputSpec, solnSpec.length() > 0);
		System.out.println("checking desugared output against: " + solnSpec);
		final CommandLine sc = new CommandLine(solnSpec);
		final VProgram vp2 = VParser.parse(sc.readInputSpec());
		assertTrue(vp2.repOk());
		System.out.println("solution: ");
		System.out.println(vp2.toString());
		// check that the two ASTs are equivalent (logically the same)
		assertTrue("VPrograms differ " + inputSpec, vp1.equivalent(vp2));
		// check examinable sanity
		ExaminableProperties.checkAllUnary(vp1);
		ExaminableProperties.checkAllUnary(vp2);
		ExaminableProperties.checkAllBinary(vp1, vp2);
		// success!
		System.out.println("accepted, as expected:  " + inputSpec);
	}

	private final static class DesugarCheck extends PostOrderExprVisitor {

		// a desugared tree should not contain these
		@Override public Expr visitXOr(XOrExpr e) { throw new IllegalStateException("xor not desugared"); } 
		@Override public Expr visitEqual(EqualExpr e) { throw new IllegalStateException("EqualExpr not desugared"); }
		@Override public Expr visitNAnd(NAndExpr e) { throw new IllegalStateException("nand not desugared"); }
		@Override public Expr visitNOr(NOrExpr e) { throw new IllegalStateException("nor not desugared"); }
		@Override public Expr visitXNOr(XNOrExpr e) { throw new IllegalStateException("xnor not desugared"); }
		
		// these are ok in the desugared tree
		@Override public Expr visitConstant(final ConstantExpr e) { return e; }
		@Override public Expr visitVar(final VarExpr e) { return e; }
		@Override public Expr visitNot(final NotExpr e) { return e; }
		@Override public Expr visitAnd(final AndExpr e) { return e; }
		@Override public Expr visitOr(final OrExpr e) { return e; }
		@Override public Expr visitNaryAnd(final NaryAndExpr e) { return e; }
		@Override public Expr visitNaryOr(final NaryOrExpr e) { return e; }
	}
}
