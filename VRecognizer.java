package ece351.vhdl;

import org.parboiled.Rule;

import ece351.util.CommandLine;

//Parboiled requires that this class not be final
public/* final */class VRecognizer extends VBase {

	public static void main(final String arg) {
		main(new String[] { arg });
	}

	public static void main(final String[] args) {
		final CommandLine c = new CommandLine(args);
		process(VRecognizer.class, c.readInputSpec());
	}

	public Rule Program() {
		return OneOrMore(DesignUnit());
	}
	
	public Rule DesignUnit() {
		return Sequence(EntityDecl(), W0(), ArchBody(), W0());
	}
	
	public Rule EntityDecl() {
		return Sequence(IgnoreCase("entity"), W1(), Id(), W1(), "is", W1(), "port", W0(), "(", W1(), IdList(), W0(), ":", W0(), "in", W1(), "bit", W0(), ";", W1(), IdList(), W0(), ":", W0(), "out", W1(), "bit", W0(), ")", W0(), ";", W0(), "end", W1(), FirstOf( "entity", Id() ),  W0(), ";");
	}
	
	public Rule IdList() {
		return Sequence(Id(), W0(), ZeroOrMore(",", W0(), Id()));
	}
	
	public Rule ArchBody() {
		return Sequence(IgnoreCase("architecture"), W1(), Id(), W1(), "of", W1(), Id(), W1(), "is", W1(), Optional(Sequence("signal", W1(), IdList(), W0(), ":", W0(), "bit", W0(), ";")), "begin", W1(), ZeroOrMore(CompInst(),  W0()), FirstOf(ProcessStmts(), SigAssnStmts()), W0(), "end", W1(), Id(), W0(), ";");
	}
	
	public Rule SigAssnStmts() {
		return OneOrMore(SigAssnStmt(),  W0());
	}
	
	public Rule SigAssnStmt() {
		return Sequence(Id(), W0(), "<=", W0(), Expr(), W0(), ";");
	}
	
	public Rule ProcessStmts() {
		return OneOrMore(ProcessStmt(),  W0());
	}
	
	public Rule ProcessStmt() {
		return Sequence("process", W0(), "(", W0(), IdList(), W0(), ")", W0(), "begin",  W1(), FirstOf( IfElseStmts(), SigAssnStmts() ), "end", W1(), "process", W0(), ";");
	}
	
	public Rule IfElseStmts() {
		return OneOrMore(IfElseStmt(), W0());
	}
	
	public Rule IfElseStmt() {
		return Sequence("if", W0(), Expr(), W0(), "then",  W1(), SigAssnStmts(),  W0(), "else",  W1(), SigAssnStmts(), W0(), "end", W1(), "if", Optional(Id(), W0()), ";");
	}
	
	public Rule CompInst() {
		return Sequence(Id(), ":", "entity", "work.", Id(), "port", "map", "(", IdList(), ")", ";");
	}
	
	public Rule Expr() {
		return Sequence(XnorTerm(), ZeroOrMore(Sequence(W1(), IgnoreCase("xnor"), W1(), XnorTerm())));
	}
	
	public Rule XnorTerm() {
		return Sequence(XorTerm(), ZeroOrMore(Sequence(W1(), IgnoreCase("xor"), W1(), XorTerm())));
	}
	
	public Rule XorTerm() {
		return Sequence(NorTerm(), ZeroOrMore(Sequence(W1(), IgnoreCase("nor"), W1(), NorTerm())));
	} 
	
	public Rule NorTerm() {
		return Sequence(NandTerm(), ZeroOrMore(Sequence(W1(),IgnoreCase("nand"), W1(), NandTerm())));
	} 
	
	public Rule NandTerm() {
		return Sequence(OrTerm(), ZeroOrMore(Sequence(W1(),IgnoreCase("or"), W1(), OrTerm())));
	} 
	
	public Rule OrTerm() {
		return Sequence(AndTerm(), ZeroOrMore(Sequence(W1(),IgnoreCase("and"), W1(), AndTerm())));
	} 
	
	public Rule AndTerm() {
		return Sequence(EqTerm(), ZeroOrMore(Sequence(W0(),"=", W0(), EqTerm())));
	} 
	
	public Rule EqTerm() {
		return FirstOf( 
				Sequence(IgnoreCase("not"), W0(), EqTerm()),
				Sequence("(", W0(), Expr(), W0(), ")"),
				Var(),
				Constant()
		);
	}
	
	public Rule Var() {
		return Id();
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
    	return FirstOf("'0'", "'1'");
    }
	
	public Rule Digit() {
    	return CharRange('0','9');
    }

}
