package br.com.caelum.agiletickets.domain.precos;

import java.math.BigDecimal;

import br.com.caelum.agiletickets.models.Sessao;
import br.com.caelum.agiletickets.models.TipoDeEspetaculo;

public class CalculadoraDePrecos {

	private static final BigDecimal TAXA_REAJUSTE_ACABANDO = BigDecimal
			.valueOf(0.10);
	private static final BigDecimal TAXA_REAJUSTE_BALLET_60MIN = BigDecimal
			.valueOf(0.10);
	private static final BigDecimal TAXA_REAJUSTE_ORQUESTRA_60MIN = BigDecimal
			.valueOf(0.10);
	private static final BigDecimal TAXA_REAJUSTE_BALLET = BigDecimal
			.valueOf(0.20);
	private static final BigDecimal TAXA_REAJUSTE_ORQUESTRA = BigDecimal
			.valueOf(0.20);

	public static BigDecimal calculaValorFinalCompra(Sessao sessao,
			Integer quantidade) {

		switch (sessao.getEspetaculo().getTipo()) {
		case CINEMA:
			if (estaAcabandoOsIngressos(sessao)) {
				sessao.setPreco(getValorIngressoReajustado(sessao, TAXA_REAJUSTE_ACABANDO));
			}
			break;
		case SHOW:
			if (estaAcabandoOsIngressos(sessao)) {
				sessao.setPreco(getValorIngressoReajustado(sessao, TAXA_REAJUSTE_ACABANDO));
			}
			break;
		case BALLET:
			if (getPercentualOcupacao(sessao) <= 0.50) {
				sessao.setPreco(getValorIngressoReajustado(sessao,
						TAXA_REAJUSTE_BALLET));
			}
			if (sessao.getDuracaoEmMinutos() > 60) {
				sessao.setPreco(getValorIngressoReajustado(sessao,
						TAXA_REAJUSTE_BALLET_60MIN));
			}
			break;
		case ORQUESTRA:
			if (getPercentualOcupacao(sessao) <= 0.50) {
				sessao.setPreco(getValorIngressoReajustado(sessao,
						TAXA_REAJUSTE_ORQUESTRA));
			}
			if (sessao.getDuracaoEmMinutos() > 60) {
				sessao.setPreco(getValorIngressoReajustado(sessao,
						TAXA_REAJUSTE_ORQUESTRA_60MIN));
			}
			break;
		}
		return calculaValorFinalCompra(quantidade, sessao.getPreco());
	}

	private static BigDecimal calculaValorFinalCompra(Integer quantidade,
			BigDecimal preco) {
		return preco.multiply(BigDecimal.valueOf(quantidade));
	}

	private static BigDecimal getValorIngressoReajustado(Sessao sessao,
			BigDecimal valorReajuste) {
		return sessao.getPreco().add(sessao.getPreco().multiply(valorReajuste));
	}

	private static boolean estaAcabandoOsIngressos(Sessao sessao) {
		return getPercentualOcupacao(sessao) <= 0.05;
	}

	private static double getPercentualOcupacao(Sessao sessao) {
		return getTotalIngressosDisponiveis(sessao)
				/ sessao.getTotalIngressos().doubleValue();
	}

	private static int getTotalIngressosDisponiveis(Sessao sessao) {
		return sessao.getTotalIngressos() - sessao.getIngressosReservados();
	}
}