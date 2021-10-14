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
import model.dao.CategoriaVeiculoDao;
import model.entities.CategoriaVeiculo;

public class CategoriaVeiculoDaoJDBC implements CategoriaVeiculoDao{
	
	private Connection conn;	

	public CategoriaVeiculoDaoJDBC(Connection conn) {		
		this.conn = conn;
	}

	@Override
	public void insert(CategoriaVeiculo categoriaVeiculo) {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("insert into category_cars " 
			+ "(nameCategory)" 
			+ "values " 
			+ "(?)",
			Statement.RETURN_GENERATED_KEYS);

			st.setString(1, categoriaVeiculo.getNameCategory());

			int rowsaffected = st.executeUpdate();

			if (rowsaffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					categoriaVeiculo.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(CategoriaVeiculo categoriaVeiculo) {
		PreparedStatement st = null;
		
		try {
			
			st = conn.prepareStatement("update category_cars "
					+ "set nameCategory = ? "
					+ "where id = ?");
			
			st.setString(1, categoriaVeiculo.getNameCategory());
			st.setInt(2, categoriaVeiculo.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e ) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;

		try {

			st = conn.prepareStatement("delete from category_cars " + "where id = ?");

			st.setInt(1, id);
			st.executeUpdate();

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public CategoriaVeiculo findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement("select * from category_cars "
					+ "where id = ?");
			
			st.setInt(1, id);
			rs = st.executeQuery();

			if (rs.next()) {

				CategoriaVeiculo categoria = instantiateCategoriaVeiculo(rs);
				return categoria;
			}
			return null;

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	private CategoriaVeiculo instantiateCategoriaVeiculo(ResultSet rs) throws SQLException {
		CategoriaVeiculo categoriaVeiculo = new CategoriaVeiculo();
		
		categoriaVeiculo.setId(rs.getInt("id"));
		categoriaVeiculo.setNameCategory(rs.getString("nameCategory"));
		return categoriaVeiculo;
	}

	@Override
	public List<CategoriaVeiculo> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			st = conn.prepareStatement("select * from category_cars order by id");

			rs = st.executeQuery();

			List<CategoriaVeiculo> list = new ArrayList<CategoriaVeiculo>();
			Map<Integer, CategoriaVeiculo> map = new HashMap<>();

			while (rs.next()) {
				CategoriaVeiculo cat = map.get(rs.getInt("id"));
				if (cat == null) {
					cat = instantiateCategoriaVeiculo(rs);
					map.put(rs.getInt("Id"), cat);
				}
				list.add(cat);
			}
			return list;

		} catch (Exception e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	

}
