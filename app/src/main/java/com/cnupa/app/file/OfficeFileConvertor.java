package com.cnupa.app.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OfficeFileConvertor {

	public OfficeFileConvertor() {
		log.info("init MTA");
		ComThread.InitMTA();
		ComThread.startMainSTA();

	}

	@Override
	protected void finalize() throws Throwable {
		log.info("quit MTA");
		ComThread.quitMainSTA();
	}

	public void convertWordToPdf(final String inputFIle, final String outputFile) throws Exception {
		convertWord(inputFIle, outputFile, 17);

	}

	public void convertWordToHtml(final String inputFIle, final String outputFile) throws Exception {
		convertWord(inputFIle, outputFile,10);

	}

	public void convertWordToDoc(final String inputFIle, final String outputFile) throws Exception {
		convertWord(inputFIle, outputFile, 0);

	}

	public void convertWordToDocx(final String inputFIle, final String outputFile) throws Exception {
		convertWord(inputFIle, outputFile, 12);

	}

	/**
	 * 
	 * @param inputFIle
	 * @param outputFile
	 * @param outType
	 *            0:docx,8:html,12:doc,17:pdf
	 * @throws Exception
	 */
	public void convertWord(final String inputFIle, final String outputFile, Integer outType) throws Exception {

		Integer[] arr = { 0, 8, 12, 17 ,10};
		if (Arrays.asList(arr).contains(outType)) {
			saveAs(inputFIle, outputFile, "Word.Application", outType);
		} else {
			throw new Exception(inputFIle + "不支持");
		}
	}

	public void convertExcelToPdf(final String inputFIle, final String outputFile) throws Exception {
		convertExcel(inputFIle, outputFile, 57);

	}

	public void convertExcelToHtml(final String inputFIle, final String outputFile) throws Exception {
		convertExcel(inputFIle, outputFile, 44);

	}

	public void convertExcelToXls(final String inputFIle, final String outputFile) throws Exception {
		convertExcel(inputFIle, outputFile, 43);

	}

	public void convertExcelToXlsx(final String inputFIle, final String outputFile) throws Exception {
		convertExcel(inputFIle, outputFile, 51);

	}

	public void convertExcel(final String inputFIle, final String outputFile, Integer outType) throws Exception {
		Integer[] arr = { 43, 44, 51, 57 };
		if (Arrays.asList(arr).contains(outType)) {
			saveAs(inputFIle, outputFile, "Excel.Application", outType);
		} else {
			throw new Exception(inputFIle + "不支持");
		}
	}

	public void convertPowerpointToPdf(final String inputFIle, final String outputFile) throws Exception {
		convertPowerpoint(inputFIle, outputFile, 32);

	}

	public void convertPowerpointToPpt(final String inputFIle, final String outputFile) throws Exception {
		convertPowerpoint(inputFIle, outputFile, 4);

	}

	public void convertPowerpointToPptx(final String inputFIle, final String outputFile) throws Exception {
		convertPowerpoint(inputFIle, outputFile, 11);

	}

	/**
	 * 
	 * @param inputFIle
	 * @param outputFile
	 * @param outType
	 *            16,17,18 gif.jpg,png
	 * @throws Exception
	 */
	public void convertPowerpoint(final String inputFIle, final String outputFile, Integer outType) throws Exception {
		Integer[] arr = { 4, 11, 16, 17, 18, 32 };
		if (Arrays.asList(arr).contains(outType)) {
			saveAs(inputFIle, outputFile, "Powerpoint.Application", outType);
		} else {
			throw new Exception(inputFIle + "不支持");
		}
	}

	public void convert2003To2007(String inputFIle) throws Exception {
		convert2003To2007(inputFIle, inputFIle + "x");

	}

	/**
	 * 自动转换
	 * 
	 * @param inputFIle
	 * @param outputFile
	 * @return
	 * @throws Exception
	 */
	public void convert2003To2007(final String inputFIle, final String outputFile) throws Exception {
		log.info("转换 " + inputFIle + " 到 " + outputFile);
		String appName = null;
		int newFileType = 0;

		String fileType = inputFIle.substring(inputFIle.lastIndexOf("."), inputFIle.length());

		if (fileType.equals(".doc")) {
			appName = "Word.Application";
			newFileType = 12;

		} else if (inputFIle.endsWith(".docx")) {
			appName = "Word.Application";
			newFileType = 0;
		} else if (inputFIle.endsWith(".xls")) {
			appName = "Excel.Application";
			newFileType = 51;
		} else if (inputFIle.endsWith(".xlsx")) {
			appName = "Excel.Application";
			newFileType = 43;
		} else if (inputFIle.endsWith(".ppt")) {
			appName = "Powerpoint.Application";
			newFileType = 11;
		} else if (inputFIle.endsWith(".pptx")) {
			appName = "Powerpoint.Application";
			newFileType = 4;
		} else if (fileType.equals(".pdf")) {
			appName = "Word.Application";
			newFileType = 12;
		} else if (fileType.equals(".rtf")) {
			appName = "Word.Application";
			newFileType = 12;
		} else {
			throw new Exception(inputFIle + "不支持");
		}

		saveAs(inputFIle, outputFile, appName, newFileType);

	}

	private void saveAs(final String inputFIle, final String outputFile, String appName, Integer outType)
			throws Exception {
		log.debug(inputFIle+"to"+outputFile);
		String prop = null;
		if (appName.equals("Word.Application")) {
			prop = "Documents";

		} else if (appName.endsWith("Excel.Application")) {
			prop = "Workbooks";
		} else if (appName.endsWith("Powerpoint.Application")) {
			prop = "Presentations";
		} else {
			throw new Exception(appName + "不支持");
		}
		File filein = new File(inputFIle);
		if (!filein.exists()) {
			throw new Exception(inputFIle + "不存在");
		} else {
			log.info("文件大小" + filein.length());

		}

		File file = new File(outputFile);
		if (file.exists()) {
			file.delete();
		}
		ActiveXComponent app = new ActiveXComponent(appName);
		try {
			ComThread.InitSTA();
			app.setProperty("AutomationSecurity", new Variant(3));

			Dispatch doc2 = null;
			if (prop.equals("Presentations")) {
				Dispatch doc1 = app.getProperty(prop).toDispatch();
				doc2 = Dispatch.call(doc1, "Open", inputFIle, false, false, false).toDispatch();
			} else {
				app.setProperty("Visible", false);
				Dispatch doc1 = app.getProperty(prop).toDispatch();
				doc2 = Dispatch.call(doc1, "Open", inputFIle, false, false).toDispatch();
			}
			Dispatch.call(doc2, "SaveAs", outputFile, new Variant(outType));
			Dispatch.call(doc2, "Close");
			log.info("转换 ok");
		} catch (Exception e) {
			log.error("转换失败:" + inputFIle);
			log.error(e.getMessage());
		} finally {
			app.invoke("Quit");
			ComThread.Release();
			ComThread.quitMainSTA();
		}

	}

	public void convertPdfToDocx(final String inputFIle, final String outputFile) throws Exception {
		convertWord(inputFIle, outputFile, 12);

	}

	public void convertRtfToDocx(final String inputFIle, final String outputFile) throws Exception {
		convertWord(inputFIle, outputFile, 12);

	}

	public void convertPdf(final String inputFIle, final String outputFile) throws Exception {
		File file = new File(outputFile);
		if (file.exists()) {
			file.delete();
		}
		ActiveXComponent app = new ActiveXComponent("AcroExch.PDDoc");
		try {
			try {
				ComThread.InitSTA();
				Dispatch pdfObject = app.getObject();
				Dispatch.call(pdfObject, "Open", inputFIle);
				Dispatch jso = Dispatch.call(pdfObject, "GetJSObject").toDispatch();
				Dispatch.call(jso, "SaveAs", outputFile, "com.adobe.acrobat.docx");//, "", false, false);
				Dispatch.call(pdfObject, "Close");
				log.info("转换 ok");
			} catch (Exception e) {

				e.printStackTrace();
			}
		} finally {
			app.invoke("Close");
			ComThread.Release();
			ComThread.quitMainSTA();
		}

	}

	public static void main(String[] args) {
		OfficeFileConvertor c = new OfficeFileConvertor();

		new Thread(() -> {
			try {
				File f = new File("d:\\t\\pdf");
				File[] files = f.listFiles(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return name.endsWith(".pdf");
					}
				});

				if (files == null) {
					return;
				}
				for (File fi : files) {
					String fin=fi.getAbsolutePath();
					System.out.println(fin);
					long s = System.currentTimeMillis();
					String fout = fin.substring(0,+fin.length()-4) + ".docx";
					// c.convert2003To2007("d:\\a.ppt", "d:\\11.pptx");
					// c.convert2003To2007("d:\\a.doc", "d:\\12.docx");
					// c.convertExcelToHtml("d:\\b.xlsx", "d:\\xn3.html")
					// c.convertPdfToDocx(fin,"d:\\t\\pdf\\1.docx");
					System.out.println(fout);
					try {
						c.convertPdf(fin, fout);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					long e = System.currentTimeMillis();
					System.out.println(fin + "转换" + (e - s) + "毫秒");
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}

		}).start();
	}

}