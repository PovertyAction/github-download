package org.poverty_action.github;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

abstract class ReflectiveSubjectComparator
	implements Comparator<CommitCommentSubject> {
	public abstract int compare(CommitAsSubject s0, CommitAsSubject s1);
	public abstract int compare(CommitAsSubject s0, CommitLine s1);
	public abstract int compare(CommitLine s0, CommitAsSubject s1);
	public abstract int compare(CommitLine s0, CommitLine s1);

	@Override
	public int compare(CommitCommentSubject s0, CommitCommentSubject s1) {
		Method compare;
		try {
			compare = getClass().getMethod("compare",
				s0.getClass(), s1.getClass());
		}
		catch (NoSuchMethodException|SecurityException e) {
			throw new RuntimeException(e);
		}

		try {
			return (int) compare.invoke(this, s0, s1);
		}
		catch (IllegalAccessException|IllegalArgumentException|
			InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
}
