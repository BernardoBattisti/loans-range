package ui;

import cripto.CriptoService;
import leilao.Emprestimo;
import leilao.EstadoDoEmprestimo;
import leilao.LeilaoService;
import leilao.Moeda;
import pessoa.Pessoa;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MenuUI {

    private Pessoa pessoaLogada;
    private Scanner input;

    private static final String COR_VERDE = "\u001b[32m";
    private static final String COR_AMARELA = "\u001b[33m";
    private static final String COR_AZUL = "\u001b[34m";
    private static final String COR_VERMELHA = "\u001b[31m";
    private static final String RESETAR_COR = "\u001b[0m";

    public MenuUI(Pessoa pessoaLogada, Scanner input) {
        this.pessoaLogada = pessoaLogada;
        this.input = input;
    }


    public void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("Usuário: " + pessoaLogada.getName());
            System.out.println("0- Sair (Deslogar)");
            System.out.println("1- Meus emprestimos tomados");
            System.out.println("2- Meus emprestimos selecionados (dos quais sou credor)");
            System.out.println("3- Emprestimos do sistema (Dar lance)");
            System.out.println("4- Criar emprestimo");
            System.out.println("5- Exibir operações");

            int opcao;
            try {
                opcao = Integer.parseInt(input.nextLine());
            } catch (Exception e) {
                System.out.println(COR_VERMELHA + "Opção inválida, tente novamente." + RESETAR_COR);
                continue;
            }

            try {
                switch (opcao) {
                    case 0:
                        System.out.println("Deslogando...");
                        return;
                    case 1:
                        uiListarEmprestimosTomados();
                        break;
                    case 2:
                        uiListarEmprestimosSelecionados();
                        break;
                    case 3:
                        uiListarEmprestimosSistema();
                        break;
                    case 4:
                        uiCriarEmprestimo();
                        break;
                    case 5:
                        uiListarOperacoes();
                        break;
                    default:
                        System.out.println(COR_VERMELHA + "Opção inválida, tente novamente." + RESETAR_COR);
                }
            } catch (SQLException e) {
                System.out.println(COR_VERMELHA + "ERRO DE BANCO DE DADOS: " + e.getMessage() + RESETAR_COR);
            }
        }
    }

    private void uiListarOperacoes() {
        System.out.println("\n--- Histórico de Operações ---");
        List<String> operacoes = pessoaLogada.getOperacoes();
        if (operacoes.isEmpty()) {
            System.out.println("Nenhuma operação registrada.");
            return;
        }

        int count = 0;
        for (String operacao : operacoes) {
            System.out.println("-------------------------------------------------------------------------");
            System.out.println(count + "- " + operacao);
            count++;
        }
    }

    private void uiListarEmprestimosSelecionados() throws SQLException {
        System.out.println("\n--- Empréstimos Selecionados (Lances) ---");
        System.out.println("Tabela de cores:");
        System.out.println(COR_VERDE + "   Leilão Vencido (Você ganhou!)" + RESETAR_COR);
        System.out.println(COR_VERMELHA + "   Leilão Perdido!" + RESETAR_COR);
        System.out.println(COR_AZUL + "   Ganhando o Leilão (Aberto)" + RESETAR_COR);
        System.out.println(COR_AMARELA + "   Perdendo o leilão (Aberto)" + RESETAR_COR);

        String corEmprestimo = null;
        List<Emprestimo> selecionados = LeilaoService.getEmprestimosPorCredor(pessoaLogada.getId());

        if (selecionados.isEmpty()) {
            System.out.println("Nenhum emprestimo foi encontrado!");
            return;
        }

        int count = 0;
        for (Emprestimo emprestimo : selecionados) {
            if (pessoaLogada.equals(emprestimo.getCredor())) {
                corEmprestimo = COR_AZUL;
                if (emprestimo.getEstadoDoEmprestimo().equals(EstadoDoEmprestimo.FECHADO)) {
                    corEmprestimo = COR_VERDE;
                }
            } else {
                corEmprestimo = COR_AMARELA;
                if (emprestimo.getEstadoDoEmprestimo().equals(EstadoDoEmprestimo.FECHADO)) {
                    corEmprestimo = COR_VERMELHA;
                }
            }
            System.out.print("\n" + corEmprestimo + count + "- " + emprestimo.toString() + RESETAR_COR);
            count++;
        }
    }

    private void uiListarEmprestimosTomados() throws SQLException {
        System.out.println("\n--- Meus Empréstimos Tomados ---");
        List<Emprestimo> tomados = LeilaoService.getEmprestimosPorTomador(pessoaLogada.getId());

        if (tomados.isEmpty()) {
            System.out.println("Nenhum emprestimo foi encontrado!");
            return;
        }

        int count = 1;
        for (Emprestimo emprestimo : tomados) {
            if (LeilaoService.prazoDePagamentoAtingido(emprestimo.getId())) {
                System.out.println(COR_VERMELHA + count + "- " + emprestimo.toString() + RESETAR_COR);
            } else {
                System.out.println(count + "- " + emprestimo.toString());
            }
            count++;
        }

        System.out.println("\nDigite o número de um empréstimo para ver opções ou [0] para sair: ");
        int emprestimoSelecionadoNum;
        try {
            emprestimoSelecionadoNum = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            System.out.println(COR_VERMELHA + "Resposta invalida" + RESETAR_COR);
            return;
        }

        if (emprestimoSelecionadoNum == 0) return;

        Emprestimo emprestimo;
        try {
            emprestimo = tomados.get(emprestimoSelecionadoNum - 1);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(COR_VERMELHA + "Emprestimo inexistente!" + RESETAR_COR);
            return;
        }

        if (LeilaoService.prazoDePagamentoAtingido(emprestimo.getId())) {
            System.out.println(COR_VERMELHA + "\nATENÇÃO: Emprestimo NÃO PAGO!" + RESETAR_COR);
        }

        System.out.println("\n--- Opções do Empréstimo ---");
        System.out.println("0- Voltar");
        System.out.println("1- Finalizar Leilão (Se for o tomador)");
        System.out.println("2- Pagar Fatura (Se o leilão estiver fechado)");

        int opcao;
        try {
            opcao = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            System.out.println(COR_VERMELHA + "Opção inválida." + RESETAR_COR);
            return;
        }

        switch (opcao) {
            case 0:
                System.out.println("Voltando...");
                break;
            case 1:
                if (emprestimo.getEstadoDoEmprestimo().equals(EstadoDoEmprestimo.FECHADO))
                {
                    System.err.println("Já esta fechado!!");
                    break;
                }
                if (emprestimo.getTomador().equals(pessoaLogada)) {
                    try {
                        LeilaoService.encerrarLeilao(emprestimo);
                        pessoaLogada.adicionarOperacao("Um leilao foi finalizado!");
                        System.out.println(COR_VERDE + "Leilão finalizado com sucesso!" + RESETAR_COR);
                    } catch (SQLException e) {
                        System.out.println(COR_VERMELHA + "Erro ao finalizar leilão: " + e.getMessage() + RESETAR_COR);
                    }
                } else {
                    System.out.println(COR_VERMELHA + "Você não é o tomador deste empréstimo." + RESETAR_COR);
                }
                break;
            case 2:
                if (emprestimo.getEstadoDoEmprestimo() == EstadoDoEmprestimo.ABERTO) {
                    System.err.println("O Emprestimo ainda esta em leilão (aberto).");
                    break;
                }
                try {
                    LeilaoService.pagarFatura(emprestimo.getId());
                    pessoaLogada.adicionarOperacao("Uma fatura foi paga");
                    System.out.println(COR_VERDE + "Fatura paga com sucesso!" + RESETAR_COR);
                } catch (SQLException e) {
                    System.out.println(COR_VERMELHA + "Erro ao pagar fatura: " + e.getMessage() + RESETAR_COR);
                }
                break;
            default:
                System.out.println(COR_VERMELHA + "Opção inválida!" + RESETAR_COR);
        }
    }

    private void uiCriarEmprestimo() throws SQLException {
        System.out.println("\n--- Criar Novo Empréstimo ---");
        try {
            System.out.print("Moedas Disponíveis: ");
            for (Moeda moeda : Moeda.values()) {
                System.out.print(moeda.name() + " | ");
            }

            System.out.println("\nInsira qual moeda deseja receber:");
            Moeda moedaSolicitada = Moeda.valueOf(input.nextLine().toUpperCase());

            System.out.println("Insira a quantidade que deseja receber:");
            double quantidadeSolicitada = Double.parseDouble(input.nextLine());

            System.out.println("Digite o juros inicial: (credores disputarão por juros menores)");
            double jurosInicial = Double.parseDouble(input.nextLine());

            System.out.println("Qual moeda deseja colocar na garantia:");
            Moeda moedaGarantia = Moeda.valueOf(input.nextLine().toUpperCase());

            System.out.println("Insira quantas moedas deseja colocar na garantia:");
            double quantidadeGarantia = Double.parseDouble(input.nextLine());

            if (!verificarValorGarantiaValido(moedaGarantia, quantidadeGarantia, moedaSolicitada, quantidadeSolicitada)) {
                System.out.println(COR_VERMELHA + "Valor da garantia inválido! (Deve ser >= 150% do valor solicitado)" + RESETAR_COR);
                return;
            }

            System.out.println("Digite o número de parcelas: ");
            int quantidadeParcelas = Integer.parseInt(input.nextLine());
            if (quantidadeParcelas <= 0) {
                System.out.println(COR_VERMELHA + "O valor deve ser maior que zero!" + RESETAR_COR);
                return;
            }

            LeilaoService.criarEmprestimo(jurosInicial, pessoaLogada, moedaSolicitada, quantidadeSolicitada,
                    moedaGarantia, quantidadeGarantia, quantidadeParcelas);

            pessoaLogada.adicionarOperacao("Emprestimo criado solicitando " + moedaSolicitada.getSimbolo() + " " + quantidadeSolicitada);
            System.out.println(COR_VERDE + "Emprestimo adicionado ao leilão com sucesso!" + RESETAR_COR);

        } catch (IllegalArgumentException e) {
            System.out.println(COR_VERMELHA + "Erro: Valor ou tipo de moeda inválido." + RESETAR_COR);
        } catch (Exception e) {
            System.out.println(COR_VERMELHA + "Erro inesperado: " + e.getMessage() + RESETAR_COR);
        }
    }

    private void uiListarEmprestimosSistema() throws SQLException {
        System.out.println("\n--- Empréstimos Disponíveis no Sistema ---");
        List<Emprestimo> emprestimosDoSistema = LeilaoService.getEmprestimosAbertos();

        if (emprestimosDoSistema.isEmpty()) {
            System.out.println("Não existem emprestimos no sistema!");
            return;
        }

        for (Emprestimo emprestimo : emprestimosDoSistema) {
            System.out.println("---------------------------------");
            System.out.print((emprestimosDoSistema.indexOf(emprestimo) + 1) + "- ");
            System.out.println("Tomador: " + emprestimo.getTomador().getName());
            System.out.println(emprestimo.toString());
        }

        uiSelecionarEmprestimoParaLance(emprestimosDoSistema);
    }

    private void uiSelecionarEmprestimoParaLance(List<Emprestimo> emprestimosDoSistema) throws SQLException {
        System.out.println("\nSelecione um dos emprestimos para dar um lance [0 para sair]: ");

        int selecionado;
        try {
            selecionado = Integer.parseInt(input.nextLine());
        } catch (Exception e) {
            System.out.println(COR_VERMELHA + "Entrada inválida" + RESETAR_COR);
            return;
        }

        if (selecionado == 0) {
            System.out.println("Voltando...");
            return;
        }

        Emprestimo emprestimo;
        try {
            emprestimo = emprestimosDoSistema.get(selecionado - 1);
            if (emprestimo.getTomador().equals(pessoaLogada)) {
                System.out.println(COR_VERMELHA + "Você não pode dar lance no seu próprio empréstimo." + RESETAR_COR);
                return;
            }
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Emprestimo inexistente!");
            return;
        }

        uiDarLance(emprestimo);
    }

    private void uiDarLance(Emprestimo emprestimo) throws SQLException {
        System.out.println("\n--- Dar Lance ---");
        System.out.println("Juros atual: " + emprestimo.getJurosAtual() + "%");
        System.out.println("É necessário um lance de juros MENOR que o atual.");
        System.out.println("Digite o valor do seu lance (ou [0] para sair):");

        double lance;
        try {
            lance = Double.parseDouble(input.nextLine());
        } catch (Exception e) {
            System.err.println("Valor de juros inválido!");
            return;
        }

        if (lance == 0) {
            System.out.println("Cancelando lance...");
            return;
        }

        if (lance >= emprestimo.getJurosAtual() || lance < 0) {
            System.err.println("Juros inválido! (Deve ser menor que o atual e maior que 0)");
            return;
        }



        LeilaoService.darLance(emprestimo.getId(), pessoaLogada.getId(), lance);
        pessoaLogada.adicionarSelecionado(emprestimo);

        System.out.println(COR_VERDE + "Lance realizado com sucesso!" + RESETAR_COR);
    }

    private boolean verificarValorGarantiaValido(Moeda moedaGarantia, double quantidadeGarantia, Moeda moedaSolicitada, double quantidadeSolicitada) {
        double valorMoedaGarantia = 0;
        double valorMoedaSolicitada = 0;
        try {
            valorMoedaGarantia = CriptoService.getPreco(moedaGarantia).getPrice();
            valorMoedaSolicitada = CriptoService.getPreco(moedaSolicitada).getPrice();
        } catch (Exception e) {
            System.err.println("Erro ao buscar preço na API: " + e.getMessage());
            return false;
        }
        double valorSolicitado = valorMoedaSolicitada * quantidadeSolicitada;
        double valorGarantia = valorMoedaGarantia * quantidadeGarantia;
        double valorMinimoGarantia = valorSolicitado * 1.5;
        return (valorGarantia >= valorMinimoGarantia);
    }
}