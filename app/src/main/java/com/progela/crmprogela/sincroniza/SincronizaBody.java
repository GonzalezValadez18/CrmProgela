package com.progela.crmprogela.sincroniza;

import java.util.List;

public class SincronizaBody {
    private int c_clientes;
    private  int c_cargos;
    private int c_vialidades;
    private int c_distribuidores;
    private int c_dominios;
    private int c_medicamentos;
    private int c_motivos;
    private int c_resultados;
    private int c_categorias;
    private int c_motivosIncompletitud;
    private int c_motivosNoSurtido;
    private int c_visitas;
    private int c_ventas;
    private int c_representantes;
    private int num_empleado;
    private List<Codigo> codigos;

    public void setNum_empleado(int num_empleado) {
        this.num_empleado = num_empleado;
    }
    public void setC_vialidades(int c_vialidades) {
        this.c_vialidades = c_vialidades;
    }
    public void setC_distribuidores(int c_distribuidores) { this.c_distribuidores = c_distribuidores;  }
    public void setC_dominios(int c_dominios) {
        this.c_dominios = c_dominios;
    }
    public void setC_medicamentos(int c_medicamentos) {
        this.c_medicamentos = c_medicamentos;
    }
    public void setC_motivos(int c_motivos) {this.c_motivos = c_motivos;}
    public void setC_resultados(int c_resultados) {this.c_resultados = c_resultados;}
    public void setC_categorias(int c_categorias) {this.c_categorias = c_categorias;}
    public void setC_motivosIncompletitud(int c_motivosIncompletitud) {this.c_motivosIncompletitud = c_motivosIncompletitud;}
    public void setC_motivosNoSurtido(int c_motivosNoSurtido) {this.c_motivosNoSurtido = c_motivosNoSurtido;}
    public void setC_visitas(int c_visitas) {this.c_visitas = c_visitas;}
    public void setC_ventas(int c_ventas) {this.c_ventas = c_ventas;}
    public void setC_representantes(int c_representantes) {this.c_representantes = c_representantes;}
    public void setC_cargos(int c_cargos) {this.c_cargos = c_cargos;}
    public void setCodigos(List<Codigo> codigos) {this.codigos = codigos;}
    public void setC_clientes(int c_clientes) {
        this.c_clientes = c_clientes;
    }
}
