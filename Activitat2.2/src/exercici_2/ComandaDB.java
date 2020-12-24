package exercici_2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ComandaDB {

	//Create the table "comandes"
	public Connection createComandesTable()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "comandes";
		
		String sentenceSQL = "CREATE TABLE ? (num_comanda VARCHAR(8),preu_total FLOAT(6,2),data DATE,dni_client VARCHAR(8),CONSTRAINT num_comanda_pk PRIMARY KEY (num_comanda),CONSTRAINT dni_client_fk FOREIGN KEY (dni_client) REFERENCES clients(dni))";
		
		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(sentenceSQL)) //Resource try-catch
			{
				ps.setString(1, tableName);
				ps.execute(sentenceSQL); //execute SQL sentence
			}
			catch(Exception e)
			{
				System.out.println("No statement -- ERROR ("+e+")");
			}			
		}
				
		return connection;
	}
	
	
	//Create the Table "resum_facturacio"
	public Connection createResum_facturacioTable()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "resum_facturacio";
		
		String sentenceSQL = "CREATE TABLE ? (mes INT,any INT,dni_client_resum VARCHAR(8),total_mes FLOAT(6,2),PRIMARY KEY (mes, any, dni_client_resum),CONSTRAINT dni_client_resum_fk FOREIGN KEY (dni_client_resum) REFERENCES comandes (dni_client) ON DELETE CASCADE);";
		
		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(sentenceSQL)) //Resource try-catch
			{
				ps.setString(1, tableName);
				ps.execute(sentenceSQL); //execute SQL sentence
			}
			catch(Exception e)
			{
				System.out.println("No statement -- ERROR ("+e+")");
			}			
		}
				
		return connection;
	}	
	
	
	//Delete all comandes
	public Connection deleteAllComandes()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "comandes";
		
		String sentenceSQL = "DELETE FROM comandes";
		
		if(connection!=null)
		{
			try(PreparedStatement statement = connection.prepareStatement(sentenceSQL)) //Resource try-catch
			{
				if(statement != null)
				{
					statement.executeUpdate(); //execute SQL sentence
					System.out.println("Table "+tableName+" cleaned -- OK");
				}
				else
				{
					System.out.println("Table "+tableName+" not cleaned -- ERROR");
				}
			}
			catch(Exception e)
			{
				System.out.println("No statement -- ERROR ("+e+")");
			}			
		}
				
		return connection;
	}	
		
	
	//Insert a Comanda (requires a Client)
	public void insertComanda(Client client, Comanda comanda)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "INSERT INTO comandes (num_comanda, preu_total,data, dni_client) values (?,?,?,?);";
		
		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query)){ //avoid SQL injection instead of using String concatenation 

				ps.setInt(1, comanda.getNum_comanda());
				ps.setFloat(2, comanda.getPreu_total());
				ps.setDate(3, java.sql.Date.valueOf(comanda.getData())); //get LocalDate and set in as Date
				ps.setString(4, client.getDni());
				ps.executeUpdate(); //execute the Update in the DB
			}
			catch(Exception e)
			{
				System.out.println("PreparedStatement error -- ERROR ("+e+")");
			}		
			finally{

				if(connection!=null)
				{
					try{
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing failed -- ERROR ("+e+")");
					}
				}
			}				
		}
	}	
	
	
	//Get Comandes from a Client
	public List<Comanda> getComandesFromClient(String dni_client)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String query = "SELECT * FROM comandes WHERE dni_client = ?;";
		List<Comanda>comandes = new ArrayList<Comanda>();
		
		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query)){ //avoid SQL injection instead of using String concatenation 
				ps.setString(1, dni_client);
				
				ResultSet rs = ps.executeQuery(); //execute the Update in the DB
				if(rs!=null)
				{
					while(rs.next())
					{
						Comanda comanda = new Comanda();
						comanda.setNum_comanda(rs.getInt("num_comanda"));
						comanda.setPreu_total(rs.getFloat("preu_total"));
						comanda.setData(rs.getDate("data").toLocalDate()); //convert LocalDate to Date
						comanda.setDni_client(rs.getString("dni_client"));
						comandes.add(comanda);
					}						
				}					
			}
			catch(Exception e)
			{
				System.out.println("Statement error -- ERROR ("+e+")");
			}		
			finally{

				if(connection!=null)
				{
					try{
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing failed -- ERROR ("+e+")");
					}
				}
			}				
		}
		return comandes;
	}		
	
	
	//Print all Comandes
	public List<Comanda> getAllComandes()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String query = "SELECT * FROM comandes;";
		
		List<Comanda> comandes = new ArrayList<Comanda>();
		
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()){ //avoid SQL injection instead of using String concatenation 
				
				ResultSet rs = statement.executeQuery(query); //execute the Update in the DB
				if(rs!=null)
				{
					while(rs.next())
					{
						Comanda comanda = new Comanda();
						comanda.setNum_comanda(rs.getInt("num_comanda"));
						comanda.setPreu_total(rs.getFloat("preu_total"));
						comanda.setData(rs.getDate("data").toLocalDate()); //convert LocalDate to Date
						comanda.setDni_client(rs.getString("dni_client")); //convert LocalDate to Date
						comandes.add(comanda);
					}						
				}
			}
			catch(Exception e)
			{
				System.out.println("Statement error -- ERROR ("+e+")");
			}		
			finally{

				if(connection!=null)
				{
					try{
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing failed -- ERROR ("+e+")");
					}
				}
			}				
		}
		return comandes;
	}	

	
	//Delete all Comandes from a Client
	public boolean deleteAllComandestFromClient(String dni)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "DELETE FROM comandes WHERE dni_client = ?;";
		boolean deletedComandes = false; 

		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query)){ //avoid SQL injection instead of using String concatenation 
				ps.setString(1, dni);
				
				int result = ps.executeUpdate();
				if(result != 0)
				{
					System.out.println("Comandes of client '"+dni+"' deleted");
				}
				else
				{
					System.out.println("No Comandes of client '"+dni+"' ");
				}
				deletedComandes = true;					
			}
			catch(Exception e)
			{
				System.out.println("Statement error -- ERROR ("+e+")");
			}		
			finally{

				if(connection!=null)
				{
					try{
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing failed -- ERROR ("+e+")");
					}
				}
			}	
		}
		return deletedComandes;
	}
	
	
	//Create the stored procedure "crea_resum_facturacio"
	public boolean createProcedureCreaResumFacturacio()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		boolean procedureCreated = false;
		
		//drop procedure query
		String queryDrop = "DROP PROCEDURE IF EXISTS crea_resum_facturacio;";
		
		//create procedure query
		String queryInsert = "CREATE PROCEDURE crea_resum_facturacio(IN p_mes INT, p_any INT) ";
		queryInsert += "BEGIN ";
		queryInsert += "DECLARE v_dni_client VARCHAR(8); ";
		queryInsert += "DECLARE v_total FLOAT(6,2); ";
		queryInsert += "DECLARE exit_loop BOOLEAN; ";
        queryInsert += "DECLARE client_cursor CURSOR FOR ";
		queryInsert += "select dni_client, format(sum(preu_total), 2) as total from comandes where extract(month from data)=p_mes and extract(year from data)=p_any group by dni_client;";
		queryInsert += "DECLARE CONTINUE HANDLER FOR NOT FOUND SET exit_loop = TRUE;";
		queryInsert += "OPEN client_cursor;";
		queryInsert += "client_loop: LOOP ";
        queryInsert += "FETCH client_cursor INTO v_dni_client, v_total;";
        queryInsert += "IF exit_loop THEN ";
		queryInsert += "CLOSE client_cursor;";
		queryInsert += "LEAVE client_loop;";
		queryInsert += "END IF;";
        queryInsert += "INSERT INTO resum_facturacio (mes,any,dni_client_resum, total_mes) values (p_mes,p_any, v_dni_client, v_total);";
        queryInsert += "END LOOP client_loop;";
        queryInsert += "END";
		
		if(connection != null)
		{
			try(PreparedStatement psDrop = connection.prepareStatement(queryDrop);
				PreparedStatement psInsert = connection.prepareStatement(queryInsert))
			{
				psDrop.execute();
				psInsert.execute();
				procedureCreated = true;
			}
			catch(Exception e)
			{
				System.out.println("Procedure not created -- ERROR ("+e+")");
			}		
			finally{

				if(connection!=null)
				{
					try{
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing failed -- ERROR ("+e+")");
					}
				}
			}
		}
		
		return procedureCreated;
	}
	
	
	//call Procedure crea_resum_facturacio
	public boolean callCreaResumFacturacio(int month, int year)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		boolean procedureCalled = false;

		if(connection != null)
		{
			CallableStatement cs = null;
			//create the call statement for a Procedure
			try
			{
				connection.setAutoCommit(false);
				cs = connection.prepareCall("{ call crea_resum_facturacio(?,?) }");
				cs.setInt(1, month);
				cs.setInt(2, year);
				
				cs.executeUpdate();
				procedureCalled = true;
			}
			catch(Exception e)
			{
				System.out.println("Procedure running ERROR ("+e+")");
				try{
					
					//undo the changes made by applying the procedure if something goes wrong
					connection.rollback();						
					System.out.println("Rollback OK");		
				}
				catch(Exception rbe)
				{
					System.out.println("Rollback ERROR ("+rbe+")");						
				}				
			}		
			finally{
				if(cs != null)
				{
					try{
						cs.close();
					}
					catch(Exception e)
					{
						System.out.println("CallableStatement ERROR ("+e+")");
					}					
				}
				
				if(connection != null)
				{
					try{
						//make a commit even though rollback has been applied
						connection.commit();
						connection.close();
					}
					catch(Exception e)
					{
						System.out.println("Connection closing ERROR ("+e+")");
					}
				}
			}			
		}
		
		return procedureCalled;
	}
	
	
	//Print all Clients
	public void printComandes()
	{
		if(0 < getAllComandes().size())
		{
			for(Comanda comanda : getAllComandes())
			{
				System.out.println(comanda.toString());
			}			
		}
	}
		
	
}
