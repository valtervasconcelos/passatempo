package com.adrianoavelar.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.apache.log4j.Logger;

import com.adrianoavelar.controller.CCadastroFilmes;

/************
 * SQLite � uma biblioteca em linguagem C que implementa um banco de dados SQL embutido.
 * Os sistemas que usam a biblioteca SQLite podem ter acesso a banco de dados SQL sem executar um 
 * SGBD (Sistema Gerenciador de Banco de Dados) separado como MySQL, PostgreSQL, Oracle.
 * SQLite n�o � uma biblioteca cliente usada para conectar com um grande servidor de banco de dados, 
 * mas sim o pr�prio servidor. A biblioteca SQLite l� e escreve diretamente para e do arquivo do banco de dados no disco.
 * @author Adriano
 * @see Para mais informa��es {@Link http://www.sqlite.org/datatype3.html}
 ************/
public class SQLiteDao  implements IDAO {

	private static Logger LOG = Logger.getLogger(SQLiteDao.class);
	private SQLiteHandler sqlite;
	public SQLiteDao() {
		sqlite = new SQLiteHandler();
		sqlite.openDB("main.sqlite");
	}

	/* (non-Javadoc)
	 * @see com.adrianoavelar.model.IDAO#salvar(com.adrianoavelar.controller.Cliente)
	 */
	@Override
	public void salvar(Cliente client) throws SQLException {

		String sql = "";
		
		sql = "INSERT INTO main.clientes("
				+ "nome,endereco,bairro,cidade,estado,cpf,cep,tel,sexo) "
				+ "VALUES("+extractClienteInfo(client)+ ");";
		
		//LOG.debug("SQL Gerado: "+sql);
		sqlite.executeUpdate(sql);
	}
	
	/**
	 * Este m�todo extrai informa��es de um cliente para colocar
	 * no bando de dados.
	 * @param 
	 * Object da classe {@link com.adrianoavelar.model.Cliente}
	 * @return
	 * String formatada com os valores a serem colocados no bando de dados.
	 */
	private String extractClienteInfo(Cliente client){
		
		StringBuilder out = new StringBuilder();
		out.append("'"+client.getNome()+"',");
		out.append("'"+client.getEndereco()+"',");
		out.append("'"+client.getBairro()+"',");
		out.append("'"+client.getCidade()+"',");
		out.append("'"+client.getEstado()+"',");
		out.append(client.getCPF()+",");
		out.append(client.getCEP()+",");
		out.append(client.getTel()+",");
		out.append("'"+client.getSexo().toUpperCase()+"'");
		return out.toString();
	}

	@Override
	public void atualizar(Cliente client) {
		// TODO Auto-generated method stub
	}

	@Override
	public void remover(Cliente client) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Cliente> pesquisar(Cliente client) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void finalize() throws Throwable {
		sqlite.closeDB();
		super.finalize();
	}

	@Override
	public void salvar(Filme filme) throws SQLException {
	String sql = "";
		
		sql = "INSERT INTO main.filmes("
				+ "titulo_original,titulo_traduzido,duracao,ano,idioma,genero,classificacao,imagem) "
				+ "VALUES("+extractFilmeInfo(filme)+ ");";
		
		//LOG.debug("SQL Gerado: "+sql);
		sqlite.executeUpdate(sql);
	}

	private String extractFilmeInfo(Filme filme) {
		StringBuilder out = new StringBuilder();
		out.append("'"+filme.getTituloOriginal()+"',");
		out.append("'"+filme.getTituloTraduzido()+"',");
		out.append(filme.getDuracao()+",");
		out.append(filme.getAnoDeLancamento()+",");
		out.append("'"+filme.getIdioma()+"',");
		out.append("'"+filme.getGenero()+"',");
		out.append("'"+filme.getClassificacao()+"',");
		out.append("'"+filme.getImagemFilme()+"'");
		return out.toString();
	}

	
	
	public Object pesquisaComRetorno(String coluna, String criterio, boolean withLike) {

		ResultSet rs = null;
		String sql = "SELECT transacoes.id_transacao,clientes.id_cliente,clientes.nome,filmes.titulo_original,"
				+ "  filmes.titulo_traduzido, transacoes.data FROM transacoes,clientes,filmes"
				+ " WHERE transacoes.id_cliente = clientes.id_cliente AND  transacoes.id_filme = filmes.id_filme ";
		
		if(!criterio.equals("")){
			if(withLike)
				sql += " AND "+coluna+" LIKE '%"+criterio+"%'";
			else
				sql += " AND "+coluna+" = '"+criterio+"'";
		}
		
	
		LOG.debug("Sql: "+sql);
		
		 try {
			rs = sqlite.executeQuery(sql);
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
		 return rs;
	}
	
	@Override
	public Object pesquisaComRetorno(String table, String coluna, String criterio, boolean withLike) {

		ResultSet rs = null;
		String sql = "SELECT * FROM main."+table;
		if(!criterio.equals("")){
			if(withLike)
				sql += " WHERE "+coluna+" LIKE '%"+criterio+"%'";
			else
				sql += " WHERE "+coluna+" = '"+criterio+"'";
		}
		
		LOG.debug("Sql: "+sql);
		
		 try {
			rs = sqlite.executeQuery(sql);
	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		
		 return rs;
	}

	@Override
	public void cadastrarTransacao(Transacao aluguel) throws SQLException {
		
		String sql = "";
		
		int filmesIds[] = aluguel.getFilmesIds();
		int idCliente = aluguel.getIdCliente();
		
		for(int i = 0; i < filmesIds.length; i++ ){
			sql = "INSERT INTO main.transacoes(id_cliente,id_filme) "
					+ "VALUES (" +idCliente+","+filmesIds[i]+")";
					
			LOG.debug("Sql: "+sql);
			
			sqlite.executeUpdate(sql);
		}
		
	}

}
