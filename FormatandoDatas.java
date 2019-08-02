package estudo;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.StringJoiner;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class FormatandoDatas {

	public static void main(String[] args) {
		DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime entrada;
		LocalDateTime intervalo;
		LocalDateTime retorno;
		LocalDateTime saida;
		LocalTime totalHorasExtras = LocalTime.of(0, 0);
	
		try {
			//Lendo arquivo
			CSVReader csvReader = lerArquivoCSV( Paths.get(System.getProperty("user.home"), "Documents/Processing/ponto/registros.csv") );

	        List<String[]> registros = csvReader.readAll(); // lê os registros do arquivo
	        
	        for (String[] registro : registros) {
	            imprimirLinhaArquivoCSV(registro); // mostra cada linha do arquivo
	            
	            entrada = LocalDateTime.parse(   registro[0] + " " + registro[1], formatacao ); // converte para LocalDateTime (Ex: 01/06/2019 08:00)
	            intervalo = LocalDateTime.parse( registro[0] + " " + registro[2], formatacao );
	            retorno = LocalDateTime.parse(   registro[0] + " " + registro[3], formatacao );
	            saida = LocalDateTime.parse(     registro[0] + " " + registro[5], formatacao );
	            
				/*
				 * Duration duracaoPrimeiroTurno = Duration.between(entrada, intervalo); long
				 * qtdHoras1T = duracaoPrimeiroTurno.getSeconds() / 60 / 60; long
				 * qtdHorasComAdicional = 0; LocalTime h = LocalTime.of(entrada.getHour(),
				 * entrada.getMinute()); for(int i=0; i < qtdHoras1T; i++) {
				 * if(h.isAfter(maxHoraSemAdicional) && h.isBefore(minHoraSemAdicional)) {
				 * qtdHorasComAdicional++; } h.plusHours(1); }
				 * System.out.println("qtde hs com add: "+qtdHorasComAdicional);
				 */
	            
	            if(intervalo.getHour() >= 0 && intervalo.getHour() < entrada.getHour()) {
	            	intervalo = intervalo.plusDays(1);
	            	retorno = retorno.plusDays(1);
	            	saida = saida.plusDays(1);
	            }else if(retorno.getHour() >= 0 && retorno.getHour() < entrada.getHour()) {
	            	retorno = retorno.plusDays(1);
	            	saida = saida.plusDays(1);
	            }else if(saida.getHour() >= 0 && saida.getHour() < entrada.getHour()) {
	            	saida = saida.plusDays(1);
	            }
	            
	            int qtdHorasNoturnas = calcularHorasComAdicionalNoturno(entrada, intervalo, retorno, saida);
	            
	            
	            //imprimirHorariosComData(entrada, intervalo, retorno, saida);
	            
	            LocalTime tempoTotal = LocalTime.of((int)entrada.until(saida, ChronoUnit.HOURS), (int)entrada.until(saida, ChronoUnit.MINUTES) % 60);
	            Duration duracaoIntervalo = Duration.between(intervalo, retorno);
	            long totalMinutosIntervalo = duracaoIntervalo.toMinutes();
	            LocalTime tempoReal = tempoTotal.minusMinutes(totalMinutosIntervalo);
	            LocalTime horasExtras = LocalTime.of(0, 0);
	            
	            if(tempoReal.getHour() < 8) { // se tempo menor que 8:00 (ex: 7:59)
	            	horasExtras = LocalTime.of(0, 0);
	            }else {
	            	horasExtras = tempoReal.minusHours(8);
	            	totalHorasExtras = calcularHorasExtras(totalHorasExtras, horasExtras);
	            }
	            
	            imprimirResultados(tempoTotal, tempoReal, horasExtras);
	    	}
	        
	        imprimirTotalHorasExtras(totalHorasExtras);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static int calcularHorasComAdicionalNoturno(LocalDateTime entrada, LocalDateTime intervalo, LocalDateTime retorno, LocalDateTime saida) {
		
		int qtdHs1T = 0;
        if(intervalo.getHour() < entrada.getHour()) {
        	qtdHs1T = intervalo.getHour() + 24 - entrada.getHour();
        }else {
        	qtdHs1T = intervalo.getHour() - entrada.getHour(); // obtem a quantidade de horas do 1ºT
        }
        
        int qtdHs2T = 0;
        if(saida.getHour() < retorno.getHour()) {
        	qtdHs2T = saida.getHour() + 24 - retorno.getHour();
        }else {
        	qtdHs2T = saida.getHour() - retorno.getHour(); // obtem a quantidade de horas do 1ºT
        }
        
		imprimirHorasPorTurno(qtdHs1T, qtdHs2T);
		
		int qtdHorasNoturnas = 0;
		
		LocalDateTime minHoraSemAdicional = LocalDateTime.of(entrada.getYear(), entrada.getMonth(), entrada.getDayOfMonth(), 21, 59);
		LocalDateTime maxHoraSemAdicional = LocalDateTime.of(entrada.getYear(), entrada.getMonth(), entrada.getDayOfMonth(), 5, 00).plusDays(1);
		
		//Calcula as horas noturnas do 1T
		LocalDateTime t1 = entrada;
		for(int i=0; i < qtdHs1T; i++) {
			if(t1.isAfter(minHoraSemAdicional) && t1.isBefore(maxHoraSemAdicional)) {
				qtdHorasNoturnas++;
			}
			t1 = t1.plusHours(1);
		}
		
        //Calcula as horas noturnas do 2T
		LocalDateTime t2 = retorno;
		for(int i=0; i < qtdHs2T; i++) {
			if(t2.isAfter(minHoraSemAdicional) && t2.isBefore(maxHoraSemAdicional)) {
				qtdHorasNoturnas++;
			}
			t2 = t2.plusHours(1);
		}
		
		System.out.println("Horas com adicional noturno: " + qtdHorasNoturnas + "hs");
		
		return qtdHorasNoturnas;
	}

	public static void imprimirHorasPorTurno(int qtdHs1T, int qtdHs2T) {
		StringJoiner strj = new StringJoiner("  |  ");
		strj.add("1ºT= " + qtdHs1T + "hs");
		strj.add("2ºT= " + qtdHs2T + "hs");
		System.out.println(strj.toString());
	}

	public static void imprimirTotalHorasExtras(LocalTime totalHorasExtras) {
		System.out.println("=======================================");
		System.out.println("Total Horas Extras: " + totalHorasExtras);
		System.out.println("=======================================");
	}

	public static void imprimirResultados(LocalTime tempoTotal, LocalTime tempoReal, LocalTime horasExtras) {
		System.out.println("Tempo total nominal:         " + tempoTotal);
		System.out.println("Tempo total menos intervalo: " + tempoReal);
		System.out.println("Horas extras:                " + horasExtras);
		System.out.println("-------------------------------------------------------------------------------------------");
	}

	public static LocalTime calcularHorasExtras(LocalTime totalHorasExtras, LocalTime horasExtras) {
		totalHorasExtras = totalHorasExtras.plusHours(horasExtras.getHour());
		totalHorasExtras = totalHorasExtras.plusMinutes(horasExtras.getMinute());
		return totalHorasExtras;
	}

	public static CSVReader lerArquivoCSV(Path caminho) throws IOException {
		Reader reader = Files.newBufferedReader(caminho);
		CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
		return csvReader;
	}

	public static void imprimirLinhaArquivoCSV(String[] registro) {
		System.out.println(registro[0] +
		                " | " + registro[1] +
		                " | " + registro[2] +
		                " | " + registro[3] +
		                " | " + registro[5]);
	}

	public static void imprimirHorariosComData(LocalDateTime entrada, LocalDateTime intervalo, LocalDateTime retorno, LocalDateTime saida) {
		System.out.println(entrada);
		System.out.println(intervalo);
		System.out.println(retorno);
		System.out.println(saida);
	}
	

}
