import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner teclado = new Scanner(System.in);
    private static MoradorDAO moradorDAO = new MoradorDAO();
    private static AgenteDAO agenteDAO = new AgenteDAO();
    private static ChamadoService service = new ChamadoService();

    public static void main(String[] args) {
        int opcao = -1;
        while (opcao != 0) {
            menu();
            opcao = lerInt("Escolha: ");
            try {
                switch (opcao) {
                    case 1 -> cadastrarMorador();
                    case 2 -> listarMoradores();
                    case 3 -> atualizarMorador();
                    case 4 -> excluirMorador();
                    case 5 -> cadastrarAgente();
                    case 6 -> listarAgentes();
                    case 7 -> abrirChamado();
                    case 8 -> listarChamadosDoMorador();
                    case 9 -> avancarStatus();
                    case 10 -> editarChamado();
                    case 11 -> cancelarChamado();
                    case 12 -> verHistorico();
                    case 13 -> listarPorStatus();
                    case 0 -> System.out.println("Ate mais!");
                    default -> System.out.println("Opcao invalida.");
                }
            } catch (RegraDeNegocioException e) {
                System.out.println("Aviso: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        }
    }

    private static void menu() {
        System.out.println("\n===== CHAMADO URBANO+ =====");
        System.out.println(" 1 - Cadastrar morador");
        System.out.println(" 2 - Listar moradores");
        System.out.println(" 3 - Atualizar morador");
        System.out.println(" 4 - Excluir morador");
        System.out.println(" 5 - Cadastrar agente municipal");
        System.out.println(" 6 - Listar agentes");
        System.out.println(" 7 - Abrir chamado");
        System.out.println(" 8 - Listar chamados de um morador");
        System.out.println(" 9 - Avancar status de um chamado (agente)");
        System.out.println("10 - Editar chamado");
        System.out.println("11 - Cancelar chamado");
        System.out.println("12 - Ver historico de um chamado");
        System.out.println("13 - Listar chamados por status");
        System.out.println(" 0 - Sair");
    }

    private static void cadastrarMorador() throws SQLException {
        String nome = lerTexto("Nome: ");
        String email = lerTexto("E-mail: ");
        String endereco = lerTexto("Endereco: ");
        String telefone = lerTexto("Telefone: ");
        Morador m = new Morador(0, nome, telefone, endereco, email);
        moradorDAO.inserir(m);
        System.out.println("Morador cadastrado com id " + m.getId());
    }

    private static void listarMoradores() throws SQLException {
        List<Morador> lista = moradorDAO.listar();
        if (lista.size() == 0) {
            System.out.println("Nenhum morador cadastrado.");
        }
        for (Morador m : lista) {
            System.out.println(m.getDados());
        }
    }

    private static void atualizarMorador() throws RegraDeNegocioException, SQLException {
        Morador m = moradorDAO.buscarPorId(lerInt("Id do morador: "));
        if (m == null) {
            throw new RegraDeNegocioException("Morador nao encontrado.");
        }
        System.out.println("(deixe em branco para manter o valor atual)");
        String nome = lerTexto("Nome [" + m.getNome() + "]: ");
        String email = lerTexto("E-mail [" + m.getEmail() + "]: ");
        String endereco = lerTexto("Endereco [" + m.getEndereco() + "]: ");
        String telefone = lerTexto("Telefone [" + m.getTelefone() + "]: ");
        if (!nome.equals("")) {
            m.setNome(nome);
        }
        if (!email.equals("")) {
            m.setEmail(email);
        }
        if (!endereco.equals("")) {
            m.setEndereco(endereco);
        }
        if (!telefone.equals("")) {
            m.setTelefone(telefone);
        }
        moradorDAO.atualizar(m);
        System.out.println("Morador atualizado.");
    }

    private static void excluirMorador() throws RegraDeNegocioException, SQLException {
        int id = lerInt("Id do morador: ");
        if (service.listarPorMorador(id).size() > 0) {
            throw new RegraDeNegocioException("Esse morador tem chamados. Cancele os chamados antes.");
        }
        moradorDAO.excluir(id);
        System.out.println("Morador excluido (se existia).");
    }

    private static void cadastrarAgente() throws SQLException {
        String nome = lerTexto("Nome: ");
        String telefone = lerTexto("Telefone: ");
        String matricula = lerTexto("Matricula: ");
        String setor = lerTexto("Setor: ");
        AgenteMunicipal a = new AgenteMunicipal(0, nome, telefone, matricula, setor);
        agenteDAO.inserir(a);
        System.out.println("Agente cadastrado com id " + a.getId());
    }

    private static void listarAgentes() throws SQLException {
        List<AgenteMunicipal> lista = agenteDAO.listar();
        if (lista.size() == 0) {
            System.out.println("Nenhum agente cadastrado.");
        }
        for (AgenteMunicipal a : lista) {
            System.out.println(a.getDados());
        }
    }

    private static void abrirChamado() throws RegraDeNegocioException, SQLException {
        int idMorador = lerInt("Id do morador: ");
        System.out.println("Tipo: 1 - Infraestrutura | 2 - Iluminacao | 3 - Limpeza");
        int tipo = lerInt("Tipo: ");
        String descricao = lerTexto("Descricao do problema: ");
        String local = lerTexto("Localizacao: ");

        String detalhe;
        if (tipo == 1) {
            detalhe = escolherOpcao("Tipo da via:", ChamadoInfraestrutura.OPCOES);
        } else if (tipo == 2) {
            detalhe = escolherOpcao("Ponto de referencia:", ChamadoIluminacao.OPCOES);
        } else if (tipo == 3) {
            detalhe = escolherOpcao("Volume estimado:", ChamadoLimpeza.OPCOES);
        } else {
            throw new RegraDeNegocioException("Tipo de chamado invalido.");
        }

        Chamado c = service.abrirChamado(idMorador, tipo, descricao, local, detalhe);
        System.out.println("Chamado aberto: " + c.getResumo());
    }

    private static void listarChamadosDoMorador() throws SQLException {
        List<Chamado> lista = service.listarPorMorador(lerInt("Id do morador: "));
        if (lista.size() == 0) {
            System.out.println("Nenhum chamado encontrado.");
        }
        for (Chamado c : lista) {
            System.out.println(c.getResumo());
        }
    }

    private static void avancarStatus() throws RegraDeNegocioException, SQLException {
        int idChamado = lerInt("Id do chamado: ");
        int idAgente = lerInt("Id do agente: ");
        Chamado c = service.avancarStatus(idChamado, idAgente);
        System.out.println("Status atualizado: " + c.getResumo());
    }

    private static void editarChamado() throws RegraDeNegocioException, SQLException {
        int idChamado = lerInt("Id do chamado: ");
        int idMorador = lerInt("Id do morador dono do chamado: ");
        String descricao = lerTexto("Nova descricao (vazio mantem): ");
        String local = lerTexto("Nova localizacao (vazio mantem): ");
        service.editarChamado(idChamado, idMorador, descricao, local);
        System.out.println("Chamado atualizado.");
    }

    private static void cancelarChamado() throws RegraDeNegocioException, SQLException {
        int idChamado = lerInt("Id do chamado: ");
        int idMorador = lerInt("Id do morador dono do chamado: ");
        service.cancelarChamado(idChamado, idMorador);
        System.out.println("Chamado cancelado.");
    }

    private static void verHistorico() throws SQLException {
        List<String> linhas = service.historico(lerInt("Id do chamado: "));
        if (linhas.size() == 0) {
            System.out.println("Sem historico.");
        }
        for (String linha : linhas) {
            System.out.println(linha);
        }
    }

    private static void listarPorStatus() throws SQLException {
        String status = escolherOpcao("Status:", StatusChamado.TODOS);
        List<Chamado> lista = service.listarPorStatus(status);
        if (lista.size() == 0) {
            System.out.println("Nenhum chamado com esse status.");
        }
        for (Chamado c : lista) {
            System.out.println(c.getResumo());
        }
    }

    private static String lerTexto(String msg) {
        System.out.print(msg);
        return teclado.nextLine().trim();
    }

    private static int lerInt(String msg) {
        System.out.print(msg);
        while (!teclado.hasNextInt()) {
            teclado.next();
            System.out.print("Digite um numero valido: ");
        }
        int numero = teclado.nextInt();
        teclado.nextLine();
        return numero;
    }

    private static String escolherOpcao(String titulo, String[] opcoes) {
        System.out.println(titulo);
        for (int i = 0; i < opcoes.length; i++) {
            System.out.println((i + 1) + " - " + opcoes[i]);
        }
        int escolha = lerInt("Opcao: ");
        while (escolha < 1 || escolha > opcoes.length) {
            System.out.println("Opcao invalida.");
            escolha = lerInt("Opcao: ");
        }
        return opcoes[escolha - 1];
    }
}
