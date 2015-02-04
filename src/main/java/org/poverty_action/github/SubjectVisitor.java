package org.poverty_action.github;

interface SubjectVisitor {
	void visit(CommitAsSubject subject);
	void visit(CommitLine subject);
}
