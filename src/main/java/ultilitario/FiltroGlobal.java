/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ultilitario;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

/**
 *
 * @author mathe
 */
@Named
@ViewScoped
public class FiltroGlobal implements Serializable {

    private String globalFilter;

    public String getGlobalFilter() {
        return globalFilter;
    }

    public void setGlobalFilter(String globalFilter) {
        this.globalFilter = globalFilter;
    }

    public boolean filterByPaciente(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        String pacienteNome = (value == null) ? null : value.toString().toLowerCase();
        return pacienteNome.contains(filterText);
    }

    public boolean filterBySUS(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.isEmpty()) {
            return true;
        }

        String susValue = (value == null) ? null : value.toString().toLowerCase();
        return susValue.contains(filterText);
    }

    public static String dataPadrao() {
        return "dd/MM/yyyy";
    }

    public static String formatoCustomizado(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(dataPadrao());
            return format.format(date);
        }
        return "";
    }

}
