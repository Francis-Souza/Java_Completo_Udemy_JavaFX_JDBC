package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.VeiculoDao;
import model.entities.CategoriaVeiculo;
import model.entities.Veiculo;
import model.entities.enums.EstadoVeiculoEnum;

public class VeiculoDaoJDBC implements VeiculoDao {
	
	private Connection conn;

	public VeiculoDaoJDBC(Connection conn) {		
		this.conn = conn;
	}

	@Override
	public void insert(Veiculo veiculo) {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("insert into veiculos "
					+ "(nameCar, colorCar, versionCar, brandCar, fabricationDate, plateCar, fuelCar, price, stateCar, categoryCarId) "
					+ "values " 
					+ "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", 
					Statement.RETURN_GENERATED_KEYS);

			st.setString(1, veiculo.getNameCar());
			st.setString(2, veiculo.getColorCar());
			st.setString(3, veiculo.getVersionCar());
			st.setString(4, veiculo.getBrandCar());
			st.setDate(5, new java.sql.Date(veiculo.getFabricationDate().getTime()));
			st.setString(6, veiculo.getPlateCar());
			st.setString(7, veiculo.getFuelCar());
			st.setDouble(8, veiculo.getPrice());
			st.setString(9, veiculo.getStateCar().name());

st.setInt(10, veiculo.getCategoriaVeiculo().getId());

			int rowsaffected = st.executeUpdate();

			if (rowsaffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					veiculo.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Veiculo veiculo) {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("update veiculos "
					+ "set nameCar = ?, colorCar = ?, versionCar = ?, brandCar = ?, fabricationDate = ?, plateCar = ?, fuelCar = ?, price = ?, stateCar = ?, categoryCarId = ? "					 
					+ "where id = ?");

			st.setString(1, veiculo.getNameCar());
			st.setString(2, veiculo.getColorCar());
			st.setString(3, veiculo.getVersionCar());
			st.setString(4, veiculo.getBrandCar());
			st.setDate(5, new java.sql.Date(veiculo.getFabricationDate().getTime()));
			st.setString(6, veiculo.getPlateCar());
			st.setString(7, veiculo.getFuelCar());
			st.setDouble(8, veiculo.getPrice());
			st.setString(9, veiculo.getStateCar().name());
			st.setInt(10, veiculo.getCategoriaVeiculo().getId());
			st.setInt(11, veiculo.getId());

			st.executeUpdate();

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		try {

			st = conn.prepareStatement("delete from veiculos where id = ?");
			st.setInt(1, id);
			st.executeUpdate();

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Veiculo findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			
			st = conn.prepareStatement("select a.*, b.nameCategory "
					+ "from veiculos a "
					+ "join category_cars b on (a.categoryCarId = b.id) "
					+ "where a.id = ?");
			st.setInt(1, id);
			
			rs = st.executeQuery();			
			if(rs.next()) {
				
				CategoriaVeiculo catCar = instantiateCategoryCar(rs);
				Veiculo car = instantiateCar( rs, catCar);
				return car;
			} 
			return null;		
			
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	private Veiculo instantiateCar( ResultSet rs, CategoriaVeiculo catCar) throws SQLException {
		Veiculo car = new Veiculo();
		car.setId(rs.getInt("id"));
		car.setNameCar(rs.getString("nameCar"));
		car.setColorCar(rs.getString("colorCar"));
		car.setVersionCar(rs.getString("versionCar"));
		car.setBrandCar(rs.getString("brandCar"));		
		car.setFabricationDate(new java.util.Date(rs.getTimestamp("fabricationDate").getTime()));
		car.setPlateCar(rs.getString("plateCar"));
		car.setFuelCar(rs.getString("fuelCar"));
		car.setPrice(rs.getDouble("price"));
		car.setEstate(EstadoVeiculoEnum.valueOf(EstadoVeiculoEnum.class, rs.getString("stateCar").toUpperCase()));
		car.setCategoriaVeiculo(catCar);
		return car;
	}

	private CategoriaVeiculo instantiateCategoryCar(ResultSet rs) throws SQLException {
		CategoriaVeiculo catCar = new CategoriaVeiculo();
		catCar.setId(rs.getInt("id"));
		catCar.setNameCategory(rs.getString("nameCategory"));
		return catCar;
	}

	@Override
	public List<Veiculo> findAll() {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select a.*, b.nameCategory "
					+ "from veiculos a "
					+ "join category_cars b on (a.id = b.id)"
					+ "order by a.id");
			rs = st.executeQuery();

			List<Veiculo> list = new ArrayList<>();
			Map<Integer, CategoriaVeiculo> map = new HashMap<>();

			while (rs.next()) {
				CategoriaVeiculo catCar = map.get(rs.getInt("categoryCarId"));
				if (catCar == null) {
					catCar = instantiateCategoryCar(rs);
					map.put(rs.getInt("categoryCarId"), catCar);
				}
				Veiculo car = instantiateCar(rs, catCar);
				list.add(car);
			}
			return list;
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(null);
		}
	}

	@Override
	public List<Veiculo> findCategoriaVeiculo(CategoriaVeiculo categoriaVeiculo) {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement("select a.*, b.nameCategory " 
			+ "from veiculos a "
			+ "join category_cars b on (a.id = b.id) " 
			+ "where b.id = ? " 
			+ "order by a.nameCar");

			st.setInt(1, categoriaVeiculo.getId());
			rs = st.executeQuery();

			List<Veiculo> list = new ArrayList<>();
			Map<Integer, CategoriaVeiculo> map = new HashMap<>();

			while (rs.next()) {

				CategoriaVeiculo catCar = map.get(rs.getInt("categoryCarId"));

				if (catCar == null) {
					catCar = instantiateCategoryCar(rs);
					map.put(rs.getInt("categoryCarId"), catCar);
				}
				Veiculo car = instantiateCar(rs, catCar);
				list.add(car);
			}
			return list;
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

}
