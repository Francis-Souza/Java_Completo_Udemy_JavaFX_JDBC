package model.entities;

import java.io.Serializable;

public class CategoriaVeiculo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String nameCategory ;
	
	public CategoriaVeiculo() {
		
	}

	public CategoriaVeiculo(Integer id, String nameCategory) {	
		this.id = id;
		this.nameCategory = nameCategory;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameCategory() {
		return nameCategory;
	}

	public void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoriaVeiculo other = (CategoriaVeiculo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CategoriaVeiculo [id=" + id + ", NameCategory=" + nameCategory + "]";
	}
	
	
	

}
