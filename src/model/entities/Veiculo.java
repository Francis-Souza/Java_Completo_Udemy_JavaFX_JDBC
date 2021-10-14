package model.entities;

import java.io.Serializable;
import java.util.Date;

import model.entities.enums.EstadoVeiculoEnum;

public class Veiculo implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String nameCar;
	private String colorCar;
	private String versionCar;
	private String brandCar;
	private Date fabricationDate;
	private String plateCar;
	private String fuelCar;
	private Double price;
	private EstadoVeiculoEnum stateCar;
	private CategoriaVeiculo categoriaVeiculo;

	public Veiculo() {

	}


	public Veiculo(Integer id, String nameCar, String colorCar, String versionCar, String brandCar,
			Date fabricationDate, String plateCar, String fuelCar, Double price, EstadoVeiculoEnum stateCar,
			CategoriaVeiculo categoriaVeiculo) {		
		this.id = id;
		this.nameCar = nameCar;
		this.colorCar = colorCar;
		this.versionCar = versionCar;
		this.brandCar = brandCar;
		this.fabricationDate = fabricationDate;
		this.plateCar = plateCar;
		this.fuelCar = fuelCar;
		this.price = price;
		this.stateCar = stateCar;
		this.categoriaVeiculo = categoriaVeiculo;
	}



	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNameCar() {
		return nameCar;
	}

	public void setNameCar(String nameCar) {
		this.nameCar = nameCar;
	}

	public String getColorCar() {
		return colorCar;
	}

	public void setColorCar(String colorCar) {
		this.colorCar = colorCar;
	}

	public String getVersionCar() {
		return versionCar;
	}

	public void setVersionCar(String versionCar) {
		this.versionCar = versionCar;
	}

	public String getBrandCar() {
		return brandCar;
	}

	public void setBrandCar(String brandCar) {
		this.brandCar = brandCar;
	}

	public Date getFabricationDate() {
		return fabricationDate;
	}

	public void setFabricationDate(Date fabricationDate) {
		this.fabricationDate = fabricationDate;
	}

	public String getPlateCar() {
		return plateCar;
	}

	public void setPlateCar(String plateCar) {
		this.plateCar = plateCar;
	}

	public String getFuelCar() {
		return fuelCar;
	}

	public void setFuelCar(String fuelCar) {
		this.fuelCar = fuelCar;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public EstadoVeiculoEnum getStateCar() {
		return stateCar;
	}
	
	public void setEstate(EstadoVeiculoEnum stateCar) {
		this.stateCar = stateCar;
	}
	
	public CategoriaVeiculo getCategoriaVeiculo() {
		return categoriaVeiculo;
	}

	public void setCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
		this.categoriaVeiculo = categoriaVeiculo;
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
		Veiculo other = (Veiculo) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Veiculo [id=" + id + ", nameCar=" + nameCar + ", colorCar=" + colorCar + ", versionCar=" + versionCar
				+ ", brandCar=" + brandCar + ", fabricationDate=" + fabricationDate + ", plateCar=" + plateCar
				+ ", fuelCar=" + fuelCar + ", price=" + price + ", stateCar=" + stateCar + ", categoriaVeiculo="
				+ categoriaVeiculo + "]";
	}

}
