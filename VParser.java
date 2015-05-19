package ece351.vhdl;

import org.parboiled.Rule;
import org.parboiled.common.ImmutableList;

import ece351.common.ast.AndExpr;
import ece351.common.ast.AssignmentStatement;
import ece351.common.ast.ConstantExpr;
import ece351.common.ast.EqualExpr;
import ece351.common.ast.Expr;
import ece351.common.ast.NAndExpr;
import ece351.common.ast.NOrExpr;
import ece351.common.ast.NotExpr;
import ece351.common.ast.OrExpr;
import ece351.common.ast.VarExpr;
import ece351.common.ast.XNOrExpr;
import ece351.common.ast.XOrExpr;
import ece351.util.CommandLine;
import ece351.vhdl.ast.Architecture;
import ece351.vhdl.ast.Component;
import ece351.vhdl.ast.DesignUnit;
import ece351.vhdl.ast.Entity;
import ece351.vhdl.ast.IfElseStatement;
import ece351.vhdl.ast.Process;
import ece351.vhdl.ast.Statement;
import ece351.vhdl.ast.VProgram;

//Parboiled requires that this class not be final
public/* final */class VParser extends VBase {

	public static void main(final String arg) {
		main(new String[] { arg });
	}

	public static void main(final String[] args) {
		final CommandLine c = new CommandLine(args);
		VProgram vprog = parse(c.readInputSpec());
		System.out.println(vprog);
	}
	 public static VProgram parse(final String[] args) {
	    	final CommandLine c = new CommandLine(args);
	    	return parse(c.readInputSpec());
	    }
	
	public static VProgram parse(final String arg) {
		return (VProgram) process(VParser.class, arg).resultValue;
	}

	public Rule Program() {
		// TODO: Write a VHDL parser that pushes an instance of VProgram to the
				// top of the stack when it is done parsing
		return Sequence(
				push(new VProgram()), 
				ZeroOrMore(DesignUnit()),
				EOI
			);
	}
	
	public Rule DesignUnit() {
		return Sequence(
				EntityDecl(),
				W0(),
				ArchBody(),
				W0(),
				push(new DesignUnit((Architecture)pop(), (Entity)pop())),
				swap(),
				push(((VProgram)pop()).append((DesignUnit)pop()))
			);
	}
	
	public Rule EntityDecl() {
		return Sequence(
				IgnoreCase("entity"), 
				W1(), 
				Id(),
				push(new Entity(match())),
				W1(),
				"is", W1(),	"port", W0(), "(", W1(),
				IdList(),
				swap(),
				push(((Entity)pop()).setInput((ImmutableList<String>)pop())),
				W0(), ":", W0(), "in", W1(), "bit", W0(), ";", W1(), 
				IdList(), 
				swap(),
				push(((Entity)pop()).setOutput((ImmutableList<String>)pop())),
				W0(), ":", W0(), "out", W1(), "bit", W0(), ")", W0(), ";", W0(), "end", W1(), 
				FirstOf( "entity", Id() ),  W0(), ";");
	}
	
	public Rule IdList() {
		return Sequence(
				Id(),  
				push((ImmutableList.of()).append((match()))), W0(),
				ZeroOrMore(W0(), ",", W0(), Id(), push(((ImmutableList<String>)pop()).append(match()))));
	}
	
	public Rule ArchBody() {
		return Sequence(
				IgnoreCase("architecture"), W1(), 
				Id(), 
				push(match()),W1(),
				"of", W1(), Id(),
				push(new Architecture(match(), (String)pop())),
				W1(), "is", W1(), 
				Optional(
						Sequence(
							IgnoreCase("signal"), W1(),
							Id(), push(((Architecture)pop()).appendSignal(match())),
							ZeroOrMore(W0(), ",", W0(), Id(), push(((Architecture)pop()).appendSignal(match()))),
							W0(), ":", W0(), IgnoreCase("bit"), W0(), ";", W0())
				), W0(), "begin", W1(), 
				ZeroOrMore(
						CompInst(),  
						swap(),
						push(((Architecture)pop()).appendComponent((Component)pop())),
						W0()
					),
				FirstOf(ProcessStmts(), SigAssnStmts()),
				swap(),
				push(((Architecture)pop()).varyStatements((ImmutableList<Statement>)pop())),
				W0(), "end", W1(), Id(), W0(), ";"
			);
	}
	
	public Rule SigAssnStmts() {
		return Sequence( 
				push(ImmutableList.of()),
				OneOrMore(
					SigAssnStmt(),
					W0(),
					swap(),
					push(((ImmutableList<Statement>)pop()).append((AssignmentStatement)pop()))
			));
	}
	
	public Rule SigAssnStmt() {
		return Sequence(
				Id(),
				push(new VarExpr(match())), W0(),
				"<=", W0(), Expr(), W0(), ";",
				swap(),
				push(new AssignmentStatement((VarExpr)pop(), (Expr)pop()))
			);
	}
	
	public Rule ProcessStmts() {
		return Sequence( 
				push(ImmutableList.of()),
				OneOrMore(
						ProcessStmt(),
					W0(),
					swap(),
					push(((ImmutableList<Process>)pop()).append((Process)pop()))
			));
	}
	
	public Rule ProcessStmt() {
		return Sequence(IgnoreCase("process"), W0(), "(", W0(), IdList(), W0(), ")",
				W0(), "begin",  W1(), 
				FirstOf(
					IfElseStmts(),
					SigAssnStmts()
				),
				push(new Process((ImmutableList<Statement>)pop(), (ImmutableList<String>)pop())),
				"end", W1(), "process", W0(), ";");
	}
	
	public Rule IfElseStmts() {
		return Sequence(
				push(ImmutableList.of()),
				OneOrMore(
					IfElseStmt(), 
					W0(),
					swap(),
					push(((ImmutableList<IfElseStatement>)pop()).append((IfElseStatement)pop()))
				)
			);
	}
	
	public Rule IfElseStmt() {
		return Sequence(
				"if", W0(), 
				Expr(), W0(), "then",  W1(),
				push(new IfElseStatement((Expr)pop())),
				SigAssnStmts(),  W0(), "else",  W1(),
				swap(),
				push(((IfElseStatement)pop()).setTrueBlock((ImmutableList<AssignmentStatement>)pop())),
				SigAssnStmts(), W0(), "end",
				swap(),
				push(((IfElseStatement)pop()).setElseBlock((ImmutableList<AssignmentStatement>)pop())),
				W1(), "if", Optional(Id(), W0()), ";");
	}
	
	public Rule CompInst() {
		return Sequence(
				Id(), push(match()), W0(), ":", W0(), IgnoreCase("entity"), W0(), "work.",
				Id(),
				push(new Component(match(), (String)pop())), W1(),
				IgnoreCase("port"), W1(), IgnoreCase("map"), W0(), "(", W0(), IdList(), W0(), ")", W0(), ";",
				swap(),
				push(((Component)pop()).varySignals((ImmutableList<String>)pop()))
			);
	}
	
	public Rule Expr() {
		return Sequence(
				XnorTerm(), ZeroOrMore(Sequence(W1(), IgnoreCase("xnor"), W1(),
				XnorTerm(), swap(),
				push(new XNOrExpr((Expr)pop(), (Expr)pop())))));
	}
	
	public Rule XnorTerm() {
		return Sequence(
				XorTerm(), ZeroOrMore(
						Sequence(W1(), IgnoreCase("xor"), W1(),
								XorTerm(),
								swap(),
								push(new XOrExpr((Expr)pop(), (Expr)pop()))
							)
						)
				);
	}
	
	public Rule XorTerm() {
		return Sequence(NorTerm(), ZeroOrMore(Sequence(W1(), IgnoreCase("nor"), W1(), NorTerm(), swap(),
				push(new NOrExpr((Expr)pop(), (Expr)pop())))));
	} 
	
	public Rule NorTerm() {
		return Sequence(NandTerm(), ZeroOrMore(Sequence(W1(),IgnoreCase("nand"), W1(), NandTerm(), swap(),
				push(new NAndExpr((Expr)pop(), (Expr)pop())))));
	} 
	
	public Rule NandTerm() {
		return Sequence(OrTerm(), ZeroOrMore(Sequence(W1(),IgnoreCase("or"), W1(), OrTerm(),swap(),
				push(new OrExpr((Expr)pop(), (Expr)pop())))));
	} 
	
	public Rule OrTerm() {
		return Sequence(AndTerm(), ZeroOrMore(Sequence(W1(),IgnoreCase("and"), W1(), AndTerm(), swap(),
				push(new AndExpr((Expr)pop(), (Expr)pop())))));
	} 
	
	public Rule AndTerm() {
		return Sequence(EqTerm(), ZeroOrMore(Sequence(W0(),"=", W0(), EqTerm(), swap(),
				push(new EqualExpr((Expr)pop(), (Expr)pop())))));
	} 
	
	public Rule EqTerm() {
		return FirstOf( 
				Sequence(IgnoreCase("not"), W0(), EqTerm(), push(new NotExpr((Expr)pop()))),
				Sequence("(", W0(), Expr(), W0(), ")"),
				Var(),
				Constant()
		);
	}
	
	public Rule Var() {
		return Sequence(Id(), push(new VarExpr(match())));
	}
	
	// For the grammar production Id, ensure that the Id does not match any
	// of the keywords specified
	// in the rule, 'Keyword'
	public Rule Id() {
		return Sequence(TestNot(Keyword()), Char(), ZeroOrMore(FirstOf(Char(), Digit(), Ch('_'))));
	}
	
	public Rule Char() {
		return FirstOf(CharRange('a','z'), CharRange('A','Z'));
	}
	
	public Rule Constant() {       
    	return FirstOf(Sequence("'0'", push(ConstantExpr.make(false))), Sequence("'1'", push(ConstantExpr.make(true))));
    }
	
	public Rule Digit() {
    	return CharRange('0','9');
    }

}
