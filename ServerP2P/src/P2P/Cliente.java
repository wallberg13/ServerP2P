/*
MIT License

Copyright (c) 2018 Wall Berg Morais, Jefferson Góes

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package P2P;

import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.registry.LocateRegistry;
import java.io.File;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * O Cliente deve possuir:
 *  - Todas as pessoas que estão conectadas sempre sendo informado pelo servidor
 *  - Interface para o Usuario: Menu usuario.
 *  - Tem uma lista de Usuarios que estão presentes, e cada usuario tem a sua lista de arquivos.
 *  - Então de inicio, desta aplicação, é solicitado um lugar para Download e outro para Upload.
 * 
 *  - Tem um proprio objeto do Cliente, que é usuario, então quando
 *    For adicionar um arquivo, já é adicionado o arquivo dentro do objeto usuario.
 * 
 * @author jefferson
 */
public class Cliente {
    
    /**
     * Objeto Servidor do tipo Interface Napster
     * E a lista de usuarios disponiveis.
     */
    private iNapster servidor;
    private ArrayList <Usuario> users;
    
    /** 
     * Localização do objeto servidor
     */
    private final String ip;
    private final Integer porta;

    /**
     * Usuario Local
     */
    private Usuario user;
    
    /** 
     * Dados utilizados somente para leitura
     */
    private String usuario;
    private String folderUp;
    private String folderDown;
    private String[] arquivosUp;
    
    /**
     * Variavel de log, toda vez que um usuario sair ou entrar, será
     * adicionado uma linha neste log.
     * Serve para não aparecer mensagens em lugares não convenientes (como por
     * exemplo, o usuario sair enquanto o outro está a escolher o que quer no menu).
     * Então o log sempre irá aparecer dps que o usuario selecionar alguma opcao do menu, ou
     * quando selecionar a opção de atualizar log que estara disponivel no menu.
     */
    private ArrayList <String> log; 
    
    /**
     * Scanner para o teclado
     */
    Scanner teclado;
    
    public Cliente(String ip, Integer porta) {
        this.ip = ip;
        this.porta = porta;
        this.teclado = new Scanner(System.in);
        
        try{
            Registry registry = LocateRegistry.getRegistry(ip, porta);
            servidor = (iNapster) registry.lookup("MeuNap");
            users = new ArrayList<>();
            
        } catch (NotBoundException | RemoteException ex) {
            System.err.println(ex);
        }
    }
    
    public static void main(String[] args) {
        Cliente c = new Cliente("127.0.0.1", 4321);
        c.executar();
    }

    private int menu(){
        int resp;
        System.out.println("--- Informações do Cliente ---");
        System.out.println("User: " + user.getNome());
        System.out.println("Pasta Upload: " + user.getDirUp());
        System.out.println("Pasta Download: " + user.getDirDown());
        System.out.println("--- Menu Usuario ---");
        System.out.println("1 - Fazer download"); // Puxa o menu download e nele já lista os arquivos disponiveis
        System.out.println("2 - Listar arquivos disponiveis");
        System.out.println("3 - Imprimir Log");
        System.out.println("0 - Sair");
        System.out.print("Digite: "); resp = teclado.nextInt();
        
        switch(resp){
            case 1:
                download();
                break;
            case 2:
                
                break;
            case 3:
                
                break;
            case 0:
                System.out.println("Aplicação Finalizada");
                break;  
            default:
                System.out.println("Opção não disponivel!!");
                break;
        }
        
        return resp;
    }
    
    /**
     * Método que tem como função definir uma interface com o usuario, 
     * ele irá definir quem 
     * 1 - Pega nome do usuario, e cria o seu proprio usuario.
     * 2 - Depois completa as informações (FolderUp, FolderDown)
     * 3 - Depois de pegar o FolderDown, então que é feito uma interação na lista
     */   
    private void executar() {
        File file;
        /**
         * Dá pra criar o objeto usuario, e dentro do construtor do usuario, 
         * ele cuida de pegar todos os arquivos
         */     
        System.out.print("Usuario: "); usuario = teclado.nextLine();        
        System.out.print("Caminho da pasta compartilhada: "); folderUp = teclado.nextLine();
        System.out.print("Caminho para salvar downloads: "); folderDown = teclado.nextLine();
        
        try{
            user = new Usuario(usuario, folderUp, folderDown, new Callback(this));
        } catch (RemoteException ex){
            System.err.println(ex);
        }
        System.out.println("Conectado com sucesso");
        //Fica executando o menu infinitamente - Ate o usuario nao quiser              
        int cond = 1;
        while(cond > 0){
            System.out.println(cond);
            cond = menu();
        }; 
        System.out.println("Finalizado");
    }   
    
    // Funções do menu
    
    /** 
     * Função que tem como objetivo listar todos os arquivos disponiveis
     */
    public void printFiles(){
        System.out.println("--- Lista de Arquivos ---");
        for(Usuario x: users){
            x.printFiles();
        }
    }
    
    /**
     * Função que tem como objetivo pedir o nome do arquivo para download, e fazer download do mesmo.
     * --- Aqui vai ter magia
     */
    public void download(){
        
    }
    
    public ArrayList<Usuario> getUsers() {
        return users;
    }
}
