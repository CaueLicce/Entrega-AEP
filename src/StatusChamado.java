public class StatusChamado {
    public static final String ABERTO = "ABERTO";
    public static final String EM_ANDAMENTO = "EM_ANDAMENTO";
    public static final String RESOLVIDO = "RESOLVIDO";
    public static final String[] TODOS = {ABERTO, EM_ANDAMENTO, RESOLVIDO};

    public static String proximo(String atual) throws RegraDeNegocioException {
        if (atual.equals(ABERTO)) {
            return EM_ANDAMENTO;
        }
        if (atual.equals(EM_ANDAMENTO)) {
            return RESOLVIDO;
        }
        throw new RegraDeNegocioException("Esse chamado ja esta resolvido.");
    }
}
