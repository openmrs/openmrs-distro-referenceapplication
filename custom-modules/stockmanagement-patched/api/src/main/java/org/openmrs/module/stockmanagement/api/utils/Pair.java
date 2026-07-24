package org.openmrs.module.stockmanagement.api.utils;

public class Pair<T1, T2> {
	
	private T1 value1;
	
	private T2 value2;
	
	public Pair(T1 value1, T2 value2) {
		this.value1 = value1;
		this.value2 = value2;
	}
	
	public T1 getValue1() {
		return value1;
	}
	
	public void setValue1(T1 value1) {
		this.value1 = value1;
	}
	
	public T2 getValue2() {
		return value2;
	}
	
	public void setValue2(T2 value2) {
		this.value2 = value2;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		
		Pair<?, ?> pair = (Pair<?, ?>) o;
		
		if (value1 != null ? !value1.equals(pair.value1) : pair.value1 != null)
			return false;
		return !(value2 != null ? !value2.equals(pair.value2) : pair.value2 != null);
		
	}
	
	@Override
	public int hashCode() {
		int result = value1 != null ? value1.hashCode() : 0;
		result = 31 * result + (value2 != null ? value2.hashCode() : 0);
		return result;
	}
}
