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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.NotBoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        this.log = new ArrayList<>();
        
        try{
            Registry registry = LocateRegistry.getRegistry(ip, porta);
            servidor = (iNapster) registry.lookup("MeuNap");
            users = new ArrayList<>();
            
        } catch (NotBoundException | RemoteException ex) {
            System.err.println(ex);
        }
    }
    
    public static void main(String[] args) {
        Cliente c = new Cliente("172.20.221.104", 4321);
        c.executar();
    }

    private Boolean menu(){
        int resp;
        System.out.println("\n--- Informações do Cliente ---");
        System.out.println("User: " + user.getNome());
        System.out.println("Pasta Upload: " + user.getDirUp());
        System.out.println("Pasta Download: " + user.getDirDown());
        System.out.println("--- Menu Usuario ---");
        System.out.println("1 - Fazer download"); // Puxa o menu download e nele já lista os arquivos disponiveis
        System.out.println("2 - Listar arquivos disponiveis");
        System.out.println("3 - Imprimir Log");
        System.out.println("0 - Sair");
        System.out.print("Digite: "); resp = teclado.nextInt();
        System.out.println("---\t---");
        switch(resp){
            case 0:
                System.out.println("Aplicação Finalizada");
                return false;
            case 1:
                String x;
                Scanner aux = new Scanner(System.in);
                System.out.print("Digite o nome do arquivo: "); 
                x = aux.nextLine();
                download(x);
                break;
            case 2:
                printFiles();
                break;
            case 3:
                printLog();
                break;
            default:
                System.out.println("Opção não disponivel!!");
                break;
        }
        return true;
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
            user.setFiles(addFiles(new File(folderUp).listFiles()));
            users = servidor.login(user);
            
        } catch (RemoteException ex){
            System.err.println(ex);
        }
        
        while(menu()); // Menu de usuario
        
        /**
         *  Informar o Servidor o logout do usuario 
         */
        try{
            servidor.logout(user);
        } catch(RemoteException ex){
            System.err.println(ex.getMessage());
        }
        
        
    }   
    
    // Funções do menu
    
    /** 
     * Função que tem como objetivo listar todos os arquivos disponiveis
     */
    public void printFiles(){
        System.out.println("\n--- Lista de Arquivos ---");
        try {
            users = servidor.getUsers();
        } catch (RemoteException ex) {
            System.err.println(ex.getMessage());
        }
        for(Usuario x: users){
            x.printFiles();
        }
        System.out.println("--- Fim de Lista de Arquivo ---");
    }
    
    /**
     * Método que tem como funcionalidade encontrar o arquivo que o usuario deseja
     * realizar o download, achar onde o arquivo está, ou mais precisamente,
     * com qual usuario está, e solitar deste o usuario um array de bytes.
     * Depois que o usuario, dono do arquivo, retorna o array de bytes do arquivo,
     * o mesmo é salvo com seu respectivo nome na pasta de download do usuario que 
     * solicitou o arquivo.
     */
    public void download(String arquivo){
        //achar de quem é o arquivo e chamar o callback de upload deste usuário
        System.out.println("\n--- Download do Arquivo " + arquivo + " ---");
        byte file[] = null;
        try {
            for(Usuario u : users) {
                for( Arquivo a : u.getFiles()) {
                    if (a.getNome().equals(arquivo)) {
                        System.out.println("Realizando Download do Arquivo");
                        file = u.getCallback().uploadFile(u, a.getNome());
                        break;
                    }
                }
            }
        } catch(RemoteException e) {
            System.err.print(e.getMessage());
        }
        if(file != null){
            // Salvando o Arquivo
            File save = new File(user.getDirDown()+"/"+arquivo);
            try{
                FileOutputStream out = new FileOutputStream(save);
                out.write(file);
                out.close();
                System.out.println("--- Download completo ---");
            } catch(FileNotFoundException ex){
                System.out.println("--- Arquivo não encontrado ---");
            } catch (IOException ex) {
                System.out.println("--- Deu Pau!! ---");
            } catch (NullPointerException ex){
                System.out.println("--- Arquivo não existe ---");
            }
        }else{
            System.out.println("--- Arquivo não encontrado ---");
        }
        
    }
    
    /**
     * Log recebido pelo servidor.
     * Este log serve para manter o usuario informado do que acontece
     * nos outros clientes, assim ele sabe quem está disponivel e quem não esta.
     */
    public void printLog(){
        System.out.println("\n--- Log do Servidor ---");
        for(String aux: log){
            System.out.println(aux);
        }
        System.out.println("--- Fim Log ---");
    }

    
    public ArrayList<String> getLog(){
        return log;
    }
    
    public ArrayList<Usuario> getUsers() {
        return users;
    }
    
    /** 
     * Método para listar todos os arquivos na pasta de upload
     * e atribui-la aos clientes.
     */
    private ArrayList<Arquivo> addFiles(File[] files) {
        ArrayList<Arquivo> x = new ArrayList<>();
        try{
            for(File y : files) 
                if(y.isFile()) x.add(new Arquivo(y.getName(), y.length()));
        } catch (NullPointerException ex){
            System.out.println("Pasta não encontrada");
        }
        return x;
    }
}
