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

/**
 *
 * @author jefferson
 */
public class Cliente {


    
    private final String ip;
    private final Integer porta;
    
//    private Usuario user;
    private String usuario;
    private String folderUp;
    private String folderDown;
    private String[] arquivosUp;
    
    public Cliente(String ip, Integer porta) {
        this.ip = ip;
        this.porta = porta;
    }
    
    public static void main(String[] args) {
        Cliente c = new Cliente("192.168.0.1", 1024);
        c.executar();
    }

    private void executar() {
        File file = null;
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
                if(y.isFile())
                    arquivosUp[i++] = y.getName();
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
        try {
//            user = new Usuario(usuario, folderUp, new CallbackImpl());
            Registry reg = LocateRegistry.getRegistry(this.ip, this.porta);
        } catch (RemoteException ex) {
            System.err.println("Erro Remoto: " + ex.getMessage());     
        }
        System.out.println("Conectado com sucesso");
        String str = "";
        String[] comando;
        
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
                    System.out.println("ls");
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
    
}
