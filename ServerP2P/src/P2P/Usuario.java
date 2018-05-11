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

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author jefferson
 */
public class Usuario implements Serializable{
    private final String nome;
    private final String dirUp;
    private final String dirDown;
    private final ICallback callback;
    private ArrayList <Arquivo> files;

    public Usuario(String nome, String dirUp, String dirDown, ICallback callback, ArrayList<Arquivo> files) {
        this.nome = nome;
        this.dirUp = dirUp;
        this.dirDown = dirDown;
        this.callback = callback;
        this.files = new ArrayList();
    }

    public String getNome() {
        return nome;
    }

    public String getDirUp() {
        return dirUp;
    }

    public String getDirDown() {
        return dirDown;
    }

    public ICallback getCallback() {
        return callback;
    }
    
    public ArrayList <Arquivo> getFiles() {
        return files;
    }
    
    public void getFiles (ArrayList<Arquivo> files) {
        this.files = files;
    }
    
    public void printFiles(){
        System.out.println("Arquivos disponiveis com: " + nome);
        for(Arquivo aux: files){
            System.out.println("\t" + aux.getNome() + " Tamanho: " + aux.getSize() + " bytes");
        }
    }
}
