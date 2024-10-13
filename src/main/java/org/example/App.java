package org.example;

import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Gson gson;
    private static ExchangeRateApi datos;
    private static final Scanner teclado = new Scanner(System.in);
    private static final float monto = 0;
    private static final String apiBase = "https://v6.exchangerate-api.com/v6";
    private static final String apikey = "/b1553fad150b7bbd879ef91a/latest/";

    public static void main( String[] args ){
        String[] opciones = {
                "ARS =>> USD",
                "USD =>> ARS",
                "AUD =>> USD",
                "BRL =>> ARS",
                "VES =>> BRL",
                "Salir"
        };

        int eleccion = menu(opciones);


        if(eleccion != (opciones.length + 1)){
            try {
                System.out.println("Ingrese el monto que desea convertir");
                monto = teclado.nextFloat();
            }catch (InputMismatchException e){
                System.out.println("Use , (coma) en lugar de . (punto) para los decimales");
            }
        }else {
            System.out.println("Cerrando el programa");
            System.exit(1);
        }

        switch (eleccion){
            case 1:
                setDatos("ARS");
                System.out.println("El valor en dolares es " + (monto * datos.USD()));
                break;
            case 2:
                setDatos("USD");
                System.out.println("El valor en pesos argentinos es " + (monto * datos.ARS()));
                break;
            case 3:
                setDatos("AUD");
                System.out.println("El valor en dolares es " + (monto * datos.USD()));
                break;
            case 4:
                setDatos("BRL");
                System.out.println("El valor en pesos argentinos es " + (monto * datos.ARS()));
                break;
            case 5:
                setDatos("VES");
                System.out.println("El valor en pesos argentinos es " + (monto * datos.BRL()));
                break;
            default:
                break;
        }


    }


    public static int menu(String ...opciones){

        System.out.println("********************");
        System.out.println("Bienvenido al conversor de monedas");

        for(int i = 0; i < opciones.length; i++){
            System.out.print((i+1) +") ");
            System.out.println(opciones[i]);
        }
        System.out.println("********************");

        try {
            int seleccion = teclado.nextInt();

            if (seleccion > opciones.length | seleccion < 0)
                throw new Exception("Numero fuera de rango");
            else
                return seleccion;

        }catch (Exception exp){
            System.out.println("Ingrese un valor correcto");
            return menu(opciones);
        }
    }
    public static void setDatos(String moneda){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(apiBase + apikey + moneda))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            client.close();

            gson = new Gson();

            //Obtengo solo el objeto conversion_rates del json y entonces lo convierto
            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject monedas = json.getAsJsonObject("conversion_rates") ;

            datos = gson.fromJson(monedas, ExchangeRateApi.class);

        }catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
