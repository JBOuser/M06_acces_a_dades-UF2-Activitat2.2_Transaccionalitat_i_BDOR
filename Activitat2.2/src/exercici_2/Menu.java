package exercici_2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Menu {
	
	private final String url = "jdbc:mysql://localhost:3306/clients_management";
	private final String user = "root";
	private final String password = "mysql";	
	private final String classForName = "com.mysql.cj.jdbc.Driver";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Menu menu = new Menu();
		menu = menu.menu(menu);
	}

	
	public Menu menu (Menu menu)
	{
		System.out.println("--MENU--");
		System.out.println("1.Eliminació d'un client (Es selecciona un client i s'elimina juntament amb les seves comandes)");
		System.out.println("2.Actualitzacio de les dades d'un client. Es selecciona el dni d'un client (PK) i s'actualitzen el seu 'nom' i 'premium'");
		System.out.println("3.Mostrar per pantalla els clients de la BBDD que comencin amb el text introduït (no hi ha case-senstitive)");
		System.out.println("4.Alta d'un nou client");
		System.out.println("5.Alta d'una nova comanda (mostrar els clients registrats i introduïr un dels dnis, i demanar les dades de la nova comanda)");		
		System.out.println("6.Mostrar per pantalla les comandes d'un client");
		System.out.println("7.Generació de resum de facturació");
		System.out.println("0.Exit");
		String option = enterData("Choose an option:"); //static method
		
		ClientDB clientDB = null;
		ComandaDB comandaDB = null;
		
		if(isInteger(option))
		{
			switch(Integer.parseInt(option))
			{
				case 1:
					clientDB = new ClientDB();
					comandaDB = new ComandaDB();
					
					//print available clients
					System.out.println("--- Available CLIENTS ---");
					for(Client client1: clientDB.getClients())
					{
						System.out.println(client1.getDni()+" - "+client1.getNom());
					}
					
					String client_id1 = enterData("Escull un client a eliminar (pel seu dni):");
					if(0 < clientDB.findClient(client_id1))
					{
						if(!comandaDB.deleteAllComandestFromClient(client_id1))
						{
							System.out.println("No Commandes for client '"+client_id1+"'");
					
						}
						if(!clientDB.deleteClientById(client_id1))
						{
							System.out.println("Client '"+client_id1+"' not deleted");
						}
					}
					else
					{
						System.out.println("Client '"+client_id1+"' not found");
					}						
					System.out.println("");							
					
					return menu.menu(menu);
					
					
				case 2:
					clientDB = new ClientDB();
					comandaDB = new ComandaDB();

					//print available clients
					System.out.println("--- Available CLIENTS ---");
					for(Client client2: clientDB.getClients())
					{
						System.out.println(client2.getDni()+" - "+client2.getNom());
					}
					
					String client_id2 = enterData("Escull un client per a actualitzar el seu nom i si és premium o no (pel seu dni):");
					Client client2 = clientDB.getClientById(client_id2);
					if(client2 != null)
					{
						client2.updateClient(false);
						if(clientDB.updateClientById(client_id2, client2))
						{
							System.out.println("Client '"+client_id2+"' updated");
						}
						else
						{
							System.out.println("Client '"+client_id2+"' not updated");							
						}
					}

					else
					{
						System.out.println("Client '"+client_id2+"' not found");
					}
					System.out.println("");							
					
					return menu.menu(menu);
				
					
				case 3:
					clientDB = new ClientDB();
					comandaDB = new ComandaDB();
					
					String string_match = enterData("Introduïr un text per saber quins noms de client comencen per aquest text (discrimina entre minúscules i majúscules):");

					List<Client> clients_match = clientDB.nameStartsWith(string_match);
					if(0 < clients_match.size())
					{
						System.out.println("Clients starting with '"+string_match+"'");	
						for(Client client : clients_match)
						{
							System.out.println(client.getDni()+" - "+client.getNom());
						}
					}
					else
					{
						System.out.println("No clients start with '"+string_match+"'");
					}
					System.out.println("");	

					return menu.menu(menu);		
				
					
				case 4:
					Client client4 = new Client();
					client4.setNewClient();

					clientDB = new ClientDB();
					clientDB.insertClient(client4);
					
					System.out.println("");
					
					return menu.menu(menu);
					
					
				case 5:
					clientDB = new ClientDB();
					comandaDB = new ComandaDB();
					
					//print available clients
					System.out.println("--- Available CLIENTS ---");
					for(Client clientTmp5 : clientDB.getClients())
					{
						System.out.println(clientTmp5.getDni()+" - "+clientTmp5.getNom());
					}
					
					String client_id5 = enterData("Escull un client per a afegir una nova comanda (pel seu dni):");
					Client client5 = clientDB.getClientById(client_id5);
					if(client5 != null)
					{
						System.out.println("Introduïr dades de la nova comanda pel Client '"+client_id5+"':");
						Comanda comanda5 = new Comanda();
						comanda5.setNewComanda(client5);
						comandaDB.insertComanda(client5, comanda5);		
						System.out.println("Comanda "+comanda5.getNum_comanda()+" created -- OK");						
					}

					else
					{
						System.out.println("Client '"+client_id5+"' not found");
					}						
					System.out.println("");					
					
					return menu.menu(menu);					
				
					
				case 6:
					clientDB = new ClientDB();
					comandaDB = new ComandaDB();
					
					//print available clients
					System.out.println("--- Available CLIENTS ---");
					for(Client client6: clientDB.getClients())
					{
						System.out.println(client6.getDni()+" - "+client6.getNom());
					}
					
					String client_id6 = enterData("Escull un client (pel seu dni):");
					
					if(clientDB.getClientById(client_id6) != null)
					{
						System.out.println("Client '"+client_id6+"'");
						
						//a list with Comanda is returned
						for(Comanda comanda6 : comandaDB.getComandesFromClient(client_id6))
						{
							if(comanda6 != null)
							{
								System.out.println(comanda6.toString());
							}
						}						
					}
					else
					{
						System.out.println("Client '"+client_id6+"' not found");
					}
					System.out.println("");
					
					return menu.menu(menu);	
					
					
				case 7:
					comandaDB = new ComandaDB();					
					String month = enterData("Introdueix el mes (1-12):");
					String year = enterData("Introdueix l'any (0001-9999):");
					
					if(comandaDB.createProcedureCreaResumFacturacio())
					{
						if(isInteger(month) && isInteger(year))
						{
							int monthInt = Integer.valueOf(month);
							int yearInt = Integer.valueOf(year);
							
							if( (1 <= monthInt && monthInt <= 12) && (1 <= yearInt && yearInt <= 9999) )
							{
								if(comandaDB.callCreaResumFacturacio(monthInt, yearInt))
								{
									System.out.println("Calling crea_resum_facturacio OK");
								}
								else
								{
									System.out.println("Calling crea_resum_facturacio ERROR");
								}
							}
							else
							{
								System.out.println("Value ERROR (01 <= mes <= 12 and 0001 <= any <= 9999)");
							}							
						}
						else
						{
							System.out.println("'mes' i 'any' should be integers (entered values: '"+month+"', '"+year+"')");
						}
					}
					else
					{
						System.out.println("Procedure creation ERROR");
					}
					System.out.println("");
					
					return menu.menu(menu);						
					
					
				case 0:
					System.out.println("Closing...");
					System.out.println("");
					
					return menu;
					
				default:
					System.out.println("Option not available '"+option+"'");
					
			}
		}
		else
		{
			System.out.println("Wrong option '"+option+"'");
		}
		System.out.println("");
		
		return menu.menu(menu);
	}
	
	//request data
	public static String enterData(String text)
	{
		System.out.println(text);
		BufferedReader br = null;
		try{
			br = new BufferedReader(new InputStreamReader(System.in));
			text = br.readLine();
		}
		catch(Exception e)
		{
			System.out.println("Read ERROR ("+e+")");
		}
		return text;
	}
	
	//check if an entered value is an integer
	public static boolean isInteger(String text)
	{
		boolean isInt = false;
		
		try{
			Integer.parseInt(text);
			isInt = true;
		}
		catch(Exception e)
		{
			//System.out.println("Entered value is not a Integer ("+e+")");
		}
		
		return isInt;
	}	
	
	//get connection object or null
	public Connection getConnection(String driver)
	{
		Connection connection = null;
		
		try {
			Class.forName(driver); //requires try-catch
			//get data from JDBC_Postgres class
			connection = DriverManager.getConnection(
					url,
					user,
					password);	
		}
		catch(Exception e)
		{
			System.out.println("MyQL Driver -- ERROR ("+e+")");			
		}
		return connection;
	}

	public String getUrl() {
		return url;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getClassForName() {
		return classForName;
	}
}
