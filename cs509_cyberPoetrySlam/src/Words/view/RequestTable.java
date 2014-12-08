package Words.view;

/**
 * Created by Jun on 12/8/2014.
 */

import Words.controller.RefreshRequestTableController;
import Words.controller.RefreshSwapTableController;
import Words.controller.SwapRemoveListener;
import Words.model.Board;
import Words.model.RequestModel;
import Words.model.SwapModel;

import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * Created by Jun on 12/8/2014. Copied WordTable of Sun
 */
public class RequestTable extends JPanel {
    /* keep eclipse happy */
    private static final long serialVersionUID = 1L;

    /** The model that "backs" the JTable. Different from Board. */
    RequestModel requestModel = null;

    /** Actual GUI entity. */
    JTable jtable = null;

    /**
     * This constructor creates the display which manages the list of active players.
     */
    public RequestTable(Board board) {

        // create the model for the data which backs this table
        requestModel = new RequestModel(board);

        // add to listener chain
        board.addListener(new RefreshRequestTableController(this));

        // the proposed dimension of the UI
        Dimension mySize = new Dimension(250, 210);

        // Scrollable panel will enclose the JTable and support scrolling vertically
        JScrollPane jsp = new JScrollPane();
        jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setPreferredSize(mySize);

        // Just add the JTable to the set. First create the list of Players,
        // then the SwingModel that supports the JTable which you now create.
        jtable = new JTable(requestModel);
        jtable.setPreferredSize(mySize);

        // let's tell the JTable about its columns.
        TableColumnModel columnModel = new DefaultTableColumnModel();

        // must build one by one...
        String[] headers = new String[] { requestModel.wordLabel, requestModel.wordTypeLabel};
        int index = 0;
        for (String h : headers) {
            TableColumn col = new TableColumn(index++);
            col.setHeaderValue(h);
            columnModel.addColumn(col);
        }
        jtable.setColumnModel(columnModel);

        // let's install a sorter and also make sure no one can rearrange columns
        JTableHeader header = jtable.getTableHeader();
        // purpose of this sorter is to sort by columns.
//        header.addMouseListener(new MouseAdapter()	{
//            public void mouseClicked (MouseEvent e)	{
//                JTableHeader h = (JTableHeader) e.getSource() ;
//                new SortController(WordTable.this).process(h, e.getPoint());
//            }
//        });

        //to add listener to handle add into swap reqeust -- JUN start===================
//        jtable.getSelectionModel().addListSelectionListener(new SwapRemoveListener(jtable, board));
        //to add listener to handle add into swap reqeust -- JUN end======================

        // Here's the trick. Make the JScrollPane take its view from the JTable.
        jsp.setViewportView(jtable);

        // add the scrolling Pane which encapsulates the JTable
        // physical size limited
        this.setPreferredSize(mySize);
        this.add(jsp);
    }

    /**
     * Causes the display of the shapes to be updated to the latest data.
     */
    public void refreshTable() {
        // THIS is the key method to ensure JTable view is synchronized
        jtable.revalidate();
        jtable.repaint();
        this.revalidate();
        this.repaint();
    }
}