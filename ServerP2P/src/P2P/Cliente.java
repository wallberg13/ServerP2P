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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.registry.LocateRegistry;
import java.io.File;
import java.rmi.NotBoundException;
import java.util.ArrayList;

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
    
    public Cliente(String ip, Integer porta) {
        this.ip = ip;
        this.porta = porta;
        
        try{
            Registry registry = LocateRegistry.getRegistry(ip, porta);
            servidor = (Napster) registry.lookup("Nap");
            users = new ArrayList<>();
            
        } catch (NotBoundException | RemoteException ex) {
            System.err.println(ex);
        }
    }
    
    public static void main(String[] args) {
        Cliente c = new Cliente("192.168.0.1", 1024);
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
        System.out.println("Digite: ");
        
        return 1;
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
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Usuario: ");
        try {
            usuario = in.readLine();
        } catch (IOException ex) {
            System.err.println("Digitação incorreta");
        }
        
        System.out.print("Caminho da pasta compartilhada: ");
        try {
            folderUp = in.readLine();
            file = new File(folderUp);
            File[] x = file.listFiles();
            Integer i = 0;
            for(File y : x) {
                if(y.isFile()) {
    //                files.add(new Arquivo(y.getName(), y.length()));
                }
            }
            
        } catch (IOException ex) {
            System.err.println("Digitação incorreta");                     
        }
        
        System.out.print("Caminho para salvar downloads: ");
        try {
            folderDown = in.readLine();
        } catch (IOException ex) {
            System.err.println("Digitação incorreta");                     
        }
        
        System.out.println("Conectado com sucesso");
        String str = "";
        String[] comando;
//        try {
//            user = new Usuario(this.usuario, folderUp, folderDown,new Callback(this), files);
//        } catch (RemoteException ex) {
//            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
//       }
        
        do {
            System.out.print(this.usuario+": ");
            try {
                str = in.readLine();
            } catch (IOException ex) {
                System.err.println("Digitação incorreta");
            }
            comando = str.split(" ");
            
            switch (comando[0]) {
                case "down":
                    //faz download de um arquivo
                    System.out.println();
                    break;
                case "ls":
                    //mostra todos os arquivos
                    System.out.println();
                    break;
                default:
                    System.out.println(comando[0] + "not found");
                    break;
            }
            
        } while(!comando[0].equals("exit"));
        
    }   
    
    /**
     * Gets e Sets
     */
    
    public ArrayList<Usuario> getUsers() {
        return users;
    }
}
