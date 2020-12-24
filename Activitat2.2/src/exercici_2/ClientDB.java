package exercici_2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ClientDB {

	//Create a the Table "Clients"
	public Connection createClientsTable()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "clients";
		
		String sentenceSQL = "CREATE TABLE clients (dni VARCHAR(8),nom VARCHAR(30),premium ENUM('S','N'),CONSTRAINT dni_pk PRIMARY KEY (dni))";
		
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()) //Resource try-catch
			{
				if(statement != null)
				{
					statement.execute(sentenceSQL); //execute SQL sentence
					System.out.println("Table "+tableName+" created -- OK");
				}
				else
				{
					System.out.println("Table "+tableName+" not created -- ERROR");
				}
			}
			catch(Exception e)
			{
				System.out.println("No statement -- ERROR ("+e+")");
			}			
		}
				
		return connection;
	}
	
	
	//Delete the content of table "Clients"
	public Connection deleteAllClients()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String tableName = "clients";
		
		String sentenceSQL = "DELETE FROM clients";
		
		if(connection!=null)
		{
			try(PreparedStatement statement = connection.prepareStatement(sentenceSQL)) //Resource try-catch
			{
				if(statement != null)
				{
					statement.execute(); //execute SQL sentence
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
		
	
	//Insert the Client to table Clients
	public void insertClient(Client client)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "INSERT INTO clients (dni,nom,premium) values (?,?,?);";
		
		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query)){ //avoid SQL injection instead of using String concatenation 

				ps.setString(1, client.getDni());
				ps.setString(2, client.getNom());
				ps.setString(3, ((client.isPremium())?"S":"N")); //if true "S", else "N"	
				ps.executeUpdate(); //execute the Update in the DB
				
				System.out.println("Client "+client.getDni()+" inserted -- OK");
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
	
	
	//Print all Clients
	public List<Client> getClients()
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		String query = "SELECT * FROM clients;";
		
		List<Client> clients = new ArrayList<Client>();
		
		if(connection!=null)
		{
			try(Statement statement = connection.createStatement()){ //avoid SQL injection instead of using String concatenation 
				
				ResultSet rs = statement.executeQuery(query); //execute the Update in the DB
				if(rs!=null)
				{
					while(rs.next())
					{
						Client client = new Client();
						client.setDni(rs.getString("dni"));
						client.setNom(rs.getString("nom"));
						client.setPremium( !rs.getString("premium").matches("0") );
						clients.add(client);
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
		return clients;
	}	
	
	
	//Check if "Client" exist
	public int findClient(String dni)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "SELECT * FROM clients WHERE dni = ?;"; //no need of single quotes ('?') 
		
		int clientFoundTimes = 0;
		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE, //object ResultSet is scrollable but not sensitive to change its data
					ResultSet.CONCUR_READ_ONLY)) //object ResultSet may not be updated
			{ 
				ps.setString(1, dni);

				ResultSet rs = ps.executeQuery(); //execute the Update in the DB
				//check got ResultSet is not null
				if(rs!=null)
				{
					//move the cursor to the last got row
					if(rs.last())
					{
						//get the position of the last row (int) which is the quantity of found tuples
						clientFoundTimes = rs.getRow();
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
		return clientFoundTimes;
	}		
	
	
	//Check if "Client" exist
	public Client getClientById(String dni)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "SELECT * FROM clients WHERE dni = ?;";
		Client client = null; 

		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE, //object ResultSet is scrollable but not sensitive to change its data
					ResultSet.CONCUR_READ_ONLY)) //object ResultSet may not be updated
			{ //avoid SQL injection instead of using String concatenation 
				ps.setString(1, dni);
				
				ResultSet rs = ps.executeQuery(); //execute the Update in the DB
				if(rs != null)
				{
					if(rs.last())
					{
						client = new Client();
						client.setDni(rs.getString("dni"));
						client.setNom(rs.getString("nom"));
						client.setPremium(rs.getString("premium").matches("S")); //if matches 'S' set true, otherwise false						
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
		return client;
	}	
	
	
	//Delete "Client" if exist
	public boolean deleteClientById(String dni)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "DELETE FROM clients WHERE dni = ?;";
		boolean deletedClient = false; 

		if(connection!=null)
		{
			try(PreparedStatement statement = connection.prepareStatement(query)){ //avoid SQL injection instead of using String concatenation 
				
				if(statement!=null)
				{
					statement.setString(1, dni);
					statement.executeUpdate(); //execute the Update in the DB. Use executeUpdate() instead of executeQuery()
					System.out.println("Client '"+dni+"' deleted");					
				}
				deletedClient = true;
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
		return deletedClient;
	}	
	
	
	//Update "Client" if exist
	public boolean updateClientById(String dni, Client client)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		
		String query = "UPDATE clients SET nom = ? , premium = ? WHERE dni = ?;";
		boolean updatedClient = false; 

		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query)){ //avoid SQL injection instead of using String concatenation 
				
				ps.setString(1, client.getNom());
				ps.setString(2, (client.isPremium() ? "S" : "N"));
				ps.setString(3, dni);
				ps.executeUpdate(); //execute the Update in the DB. Use executeUpdate() instead of executeQuery()
				updatedClient = true;
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
		return updatedClient;
	}	
	
	
	//return the "Clients" that start with the entered string
	public List<Client> nameStartsWith(String matchString)
	{
		Menu menu = new Menu();
		Connection connection = menu.getConnection(menu.getClassForName()); 
		List<Client> clients = new ArrayList<Client>();
		
		String query = "SELECT * FROM clients WHERE nom LIKE ?;";

		if(connection!=null)
		{
			try(PreparedStatement ps = connection.prepareStatement(query,
					ResultSet.TYPE_SCROLL_INSENSITIVE, //object ResultSet is scrollable but not sensitive to change its data
					ResultSet.CONCUR_READ_ONLY)) //object ResultSet may not be updated
			{ //avoid SQL injection instead of using String concatenation 
				
				ps.setString(1, matchString+"%"); //?% cannot be set in the query 
				
				ResultSet rs = ps.executeQuery();				
				if(rs!=null)
				{
					while(rs.next())
					{
						Client client = new Client();
						client.setDni(rs.getString("dni"));
						client.setNom(rs.getString("nom"));
						client.setPremium( ( rs.getString("premium").matches("S") ? true : false ) );
						clients.add(client);
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
		return clients;
	}	
	
	
	//Print all Clients
	public void printClients()
	{
		if(0 < getClients().size())
		{
			for(Client client : getClients())
			{
				System.out.println(client.toString());
			}			
		}
	}	
	
}
