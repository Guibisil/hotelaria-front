package models.ui;

import DTO.QuartoDTO;
import DTO.ReservaDTO;
import components.DsTimelineCell;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservaTimelineTableModel extends AbstractTableModel {

    private List<QuartoDTO> quartos;
    private List<LocalDate> datas;
    private Map<Long, Map<LocalDate, ReservaDTO>> mapaReservas;

    public ReservaTimelineTableModel(List<QuartoDTO> quartos, List<LocalDate> datas, List<ReservaDTO> reservas) {
        this.quartos = quartos;
        this.datas = datas;
        this.mapaReservas = new HashMap<>();

        if (reservas != null) {
            for (ReservaDTO reserva : reservas) {
                long roomId = reserva.getRoomId();
                mapaReservas.putIfAbsent(roomId, new HashMap<>());
                
                LocalDate checkin = LocalDate.parse(reserva.getCheckinDate());
                LocalDate checkout = LocalDate.parse(reserva.getCheckoutDate());
                
                LocalDate current = checkin;
                while (!current.isAfter(checkout)) {
                    mapaReservas.get(roomId).put(current, reserva);
                    current = current.plusDays(1);
                }
            }
        }
    }

    @Override
    public int getRowCount() {
        return quartos == null ? 0 : quartos.size();
    }

    @Override
    public int getColumnCount() {
        return datas == null ? 1 : datas.size() + 1;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) return "Quarto";
        LocalDate data = datas.get(column - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");
        return data.format(formatter);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (quartos == null || rowIndex >= quartos.size()) return null;
        
        QuartoDTO quarto = quartos.get(rowIndex);
        
        if (columnIndex == 0) {
            return quarto.getNumber();
        }
        
        LocalDate data = datas.get(columnIndex - 1);
        Map<LocalDate, ReservaDTO> reservasDoQuarto = mapaReservas.get(quarto.getId());
        
        if (reservasDoQuarto != null && reservasDoQuarto.containsKey(data)) {
            ReservaDTO reserva = reservasDoQuarto.get(data);
            LocalDate checkin = LocalDate.parse(reserva.getCheckinDate());
            LocalDate checkout = LocalDate.parse(reserva.getCheckoutDate());
            
            boolean isStart = data.equals(checkin) || columnIndex == 1;
            boolean isEnd = data.equals(checkout);
            
            String text = isStart ? reserva.getGuestName() : "";
            if (text == null) text = "Reserva #" + reserva.getId();
            
            return new DsTimelineCell(String.valueOf(reserva.getId()), isStart, isEnd, text);
        }
        
        return null;
    }
}
