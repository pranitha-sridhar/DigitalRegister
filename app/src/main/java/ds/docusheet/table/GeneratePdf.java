package ds.docusheet.table;

import android.os.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneratePdf {

    public Boolean PdfCreation(List<SavedData> slist, List<ColumnData> clist, String name, String doc_id, ByteArrayOutputStream stream) throws IOException {
        String filname=name+doc_id;
        String filecontent = "Contentido";
        if(Creation(filname,filecontent,slist,clist,name,stream))
            return true;
        else return false;
    }

    public Boolean Creation(String filename,String filecontent,List<SavedData> slist, List<ColumnData> clist,String name,ByteArrayOutputStream stream) throws IOException {
        try {
            String fpath = filename + ".pdf";
            File folder = new File(Environment.getExternalStorageDirectory()+"/Download");
            if(!folder.exists())
                folder.mkdir();
            File file = new File(folder.getAbsoluteFile(), fpath);

            if (!file.exists()) {
                file.createNewFile();
            }
            com.itextpdf.text.Document doc=new Document();
            PdfWriter.getInstance(doc,new FileOutputStream(file.getAbsoluteFile().toString()));
            doc.open();
            doc.addTitle(name);
            Font blue = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLUE);
            Chunk blueText = new Chunk(name, blue);
            Paragraph p=new Paragraph();

            /*PdfPTable t=new PdfPTable(2);
            PdfPCell c=new PdfPCell(new Phrase(name));
            t.setHorizontalAlignment(PdfPTable.ALIGN_TOP);
            t.addCell(c);
            */
            p.add(blueText);



            Image image= Image.getInstance(stream.toByteArray());
            image.scaleToFit(100,80);
            image.setAlignment(Image.ALIGN_TOP);
            image.setAlignment(Image.ALIGN_RIGHT);
            p.add(image);
            doc.add(p);
            PdfPTable table=new PdfPTable(clist.size());
            for(int i=0;i<clist.size();i++) {
                PdfPCell c1=new PdfPCell(new Phrase(clist.get(i).getColumn_names()));
                c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
                c1.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(c1);
            }
            for(int i=0;i<slist.size();i++)
            {
                if(!slist.get(i).getColumn1().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn1());
                }
                if(!slist.get(i).getColumn2().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn2());
                }
                if(!slist.get(i).getColumn3().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn3());
                }
                if(!slist.get(i).getColumn4().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn4());
                }
                if(!slist.get(i).getColumn5().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn5());
                }
                if(!slist.get(i).getColumn6().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn6());
                }
                if(!slist.get(i).getColumn7().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn7());
                }
                if(!slist.get(i).getColumn8().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn8());
                }
                if(!slist.get(i).getColumn9().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn9());
                }
                if(!slist.get(i).getColumn10().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn10());
                }
                if(!slist.get(i).getColumn11().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn11());
                }
                if(!slist.get(i).getColumn12().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn12());
                }
                if(!slist.get(i).getColumn13().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn13());
                }
                if(!slist.get(i).getColumn14().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn14());
                }
                if(!slist.get(i).getColumn15().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn15());
                }
                if(!slist.get(i).getColumn16().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn16());
                }if(!slist.get(i).getColumn17().equals("null"))
            {
                table.addCell(slist.get(i).getColumn17());
            }
                if(!slist.get(i).getColumn18().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn18());
                }
                if(!slist.get(i).getColumn19().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn19());
                }
                if(!slist.get(i).getColumn20().equals("null"))
                {
                    table.addCell(slist.get(i).getColumn20());
                }
            }

            doc.add(table);
            doc.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }
}
