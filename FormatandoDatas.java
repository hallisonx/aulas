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

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class FormatandoDatas {
	
	private static final double MULTIPLICADOR_AD_NOT = 1.147227533460803;
	private static final int HORAS_JORNADA_DIARIA = 7;
	private static final int MINUTOS_JORNADA_DIARIA = 20;
	
	public static void main(String[] args) {
		calcularPonto(System.getProperty("user.home") + "\\Documents\\Processing\\ponto\\registros.csv");
	}

	public static void calcularPonto(String caminhoArquivo) {
		DateTimeFormatter formatacao = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		LocalDateTime entrada;
		LocalDateTime intervalo;
		LocalDateTime retorno;
		LocalDateTime saida;
		LocalTime totalHorasExtras = LocalTime.of(0, 0);
	
		try {
			//Lendo arquivo
			CSVReader csvReader = lerArquivoCSV(Paths.get(caminhoArquivo) );
			
	        List<String[]> registros = csvReader.readAll(); // lê os registros do arquivo
	        
	        for (String[] registro : registros) {
	            imprimirLinhaArquivoCSV(registro); // mostra cada linha do arquivo
	            
	            entrada = LocalDateTime.parse(   registro[0] + " " + registro[1], formatacao ); // converte para LocalDateTime (Ex: 01/06/2019 08:00)
	            intervalo = LocalDateTime.parse( registro[0] + " " + registro[2], formatacao );
	            retorno = LocalDateTime.parse(   registro[0] + " " + registro[3], formatacao );
	            saida = LocalDateTime.parse(     registro[0] + " " + registro[4], formatacao );
	
	            
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
	            
	            int qtdHs1T = 0;
	            if(intervalo.getHour() < entrada.getHour()) {
	            	qtdHs1T = intervalo.getHour() + 24 - entrada.getHour();
	            }else {
	            	qtdHs1T = intervalo.getHour() - entrada.getHour(); // obtem a quantidade de horas do 1ºT
	            }
	            
	            int restoMin1T = (60 - entrada.getMinute()) + (intervalo.getMinute()); // 22h15 as 24h45 ; 60-15 + 60-45 = 60
	            int qtdMin1T =  (qtdHs1T *60) - 60 + restoMin1T;
            	//System.out.println(qtdHs1T +":"+ (restoMin1T-60) );
            	
	            
	            int qtdHs2T = 0;
	            if(saida.getHour() < retorno.getHour()) {
	            	qtdHs2T = saida.getHour() + 24 - retorno.getHour();
	            }else {
	            	qtdHs2T = saida.getHour() - retorno.getHour(); // obtem a quantidade de horas do 1ºT
	            }
	            
	            int restoMin2T = (60 - retorno.getMinute()) + (saida.getMinute()); // 22h15 as 24h45 ; 60-15 + 60-45 = 60
            	int qtdMin2T =  (qtdHs2T *60) - 60 + restoMin2T;
	            //System.out.println(qtdHs2T +":"+ (restoMin2T-60) );
            	
	            //imprimirHorasPorTurno(qtdHs1T, qtdHs2T);
	            
	            //int qtdHorasNoturnas = 0;
	            int qtdMinutosNoturnos = 0;
	            
	            LocalDateTime mxHoraSemAdicional = LocalDateTime.of(entrada.getYear(), entrada.getMonth(), entrada.getDayOfMonth(), 21, 59);
	            LocalDateTime mnHoraSemAdicional = LocalDateTime.of(entrada.getYear(), entrada.getMonth(), entrada.getDayOfMonth(), 5, 00).plusDays(1);
	            
	            //Calcula as horas noturnas do 1T
	            LocalDateTime t1 = entrada;
	            for(int i=0; i < qtdMin1T; i++) {
	            	if(t1.isAfter(mxHoraSemAdicional) && t1.isBefore(mnHoraSemAdicional)) {
	            		qtdMinutosNoturnos++;
	            	}
	            	t1 = t1.plusMinutes(1);
	            }
	            
	          //Calcula as horas noturnas do 2T
	            LocalDateTime t2 = retorno;
	            for(int i=0; i < qtdMin2T; i++) {
	            	if(t2.isAfter(mxHoraSemAdicional) && t2.isBefore(mnHoraSemAdicional)) {
	            		qtdMinutosNoturnos++;
	            	}
	            	t2 = t2.plusMinutes(1);
	            }
	            
	            double minutosComAd = qtdMinutosNoturnos * MULTIPLICADOR_AD_NOT;
	            
	            String horasNoturnas = ""+Math.round(qtdMinutosNoturnos / 60);
	            LocalTime tempoNot = LocalTime.of(new Integer(horasNoturnas), (int)qtdMinutosNoturnos % 60);
	            System.out.println("Hora Noturna:                " + tempoNot);
	           
	            String horasNoturnasConvert = ""+Math.round(minutosComAd / 60);
	            LocalTime tempoAdNot = LocalTime.of(new Integer(horasNoturnasConvert), (int)minutosComAd % 60);
	            System.out.println("Hora Noturna Convertida:     " + tempoAdNot);
	            	            
	            //imprimirHorariosComData(entrada, intervalo, retorno, saida);
	            
	            LocalTime tempoTotal = LocalTime.of((int)entrada.until(saida, ChronoUnit.HOURS), (int)entrada.until(saida, ChronoUnit.MINUTES) % 60);
	            Duration duracaoIntervalo = Duration.between(intervalo, retorno);
	            long totalMinutosIntervalo = duracaoIntervalo.toMinutes();
	            LocalTime tempoReal = tempoTotal.minusMinutes(totalMinutosIntervalo);
	            LocalTime horasExtras = LocalTime.of(0, 0);
	            
	            if(tempoReal.getHour() < 8) { // se tempo menor que 8:00 (ex: 7:59)
	            	horasExtras = LocalTime.of(0, 0);
	            }else {
	            	horasExtras = tempoReal.minusHours(HORAS_JORNADA_DIARIA).minusMinutes(MINUTOS_JORNADA_DIARIA);
	            	totalHorasExtras = calcularHorasExtras(totalHorasExtras, horasExtras);
	            }
	            
	            imprimirResultados(tempoTotal, tempoReal, horasExtras);
	    	}
	        
	        
	        imprimirTotalHorasExtras(totalHorasExtras);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
	    
	    
	}

	private static void imprimirHorasPorTurno(int qtdHs1T, int qtdHs2T) {
		StringJoiner strj = new StringJoiner(" | ");
		strj.add(String.valueOf(qtdHs1T));
		strj.add(String.valueOf(qtdHs2T));
		System.out.println(strj.toString());
	}

	private static void imprimirTotalHorasExtras(LocalTime totalHorasExtras) {
		System.out.println("=======================================");
		System.out.println("Total Horas Extras: " + totalHorasExtras);
		System.out.println("=======================================");
	}

	private static void imprimirResultados(LocalTime tempoTotal, LocalTime tempoReal, LocalTime horasExtras) {
		System.out.println("Tempo total nominal:         " + tempoTotal);
		System.out.println("Tempo total menos intervalo: " + tempoReal);
		System.out.println("Horas extras:                " + horasExtras);
		System.out.println("-------------------------------------------------------------------------------------------");
	}

	private static LocalTime calcularHorasExtras(LocalTime totalHorasExtras, LocalTime horasExtras) {
		totalHorasExtras = totalHorasExtras.plusHours(horasExtras.getHour());
		totalHorasExtras = totalHorasExtras.plusMinutes(horasExtras.getMinute());
		return totalHorasExtras;
	}

	private static CSVReader lerArquivoCSV(Path caminho) throws IOException {
		Reader reader = Files.newBufferedReader(caminho);
		CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
		CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(parser).withSkipLines(1).build();
		return csvReader;
	}

	private static void imprimirLinhaArquivoCSV(String[] registro) {
		System.out.println(registro[0] +
		                " | " + registro[1] +
		                " | " + registro[2] +
		                " | " + registro[3] +
		                " | " + registro[4]);
	}

	private static void imprimirHorariosComData(LocalDateTime entrada, LocalDateTime intervalo, LocalDateTime retorno, LocalDateTime saida) {
		System.out.println(entrada);
		System.out.println(intervalo);
		System.out.println(retorno);
		System.out.println(saida);
	}
	

}
