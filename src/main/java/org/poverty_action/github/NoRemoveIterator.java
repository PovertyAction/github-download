package org.poverty_action.github;

import java.util.Iterator;

class NoRemoveIterator<E> implements Iterator<E> {
	private final Iterator<? extends E> iterator;

	public NoRemoveIterator(Iterator<? extends E> iterator) {
		if (iterator == null)
			throw new NullPointerException();
		this.iterator = iterator;
	}

	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	@Override
	public E next() {
		return iterator.next();
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object o) {
		return iterator.equals(o);
	}

	@Override
	public int hashCode() {
		return iterator.hashCode();
	}
}
