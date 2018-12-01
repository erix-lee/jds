package com.cnupa.app.rest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.cnupa.app.file.OfficeFileConvertor;
import com.cnupa.app.model.Courseware;
import com.cnupa.app.model.User;
import com.cnupa.app.repo.CoursewareAppravolRepo;
import com.cnupa.app.repo.CoursewareRepo;
import com.cnupa.app.repo.CoursewareShareRepo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Transactional
@RequestMapping("courseware")
@Slf4j

public class CoursewareRest {

	@Autowired
	private CoursewareRepo coursewareRepo;
	@Autowired
	private CoursewareAppravolRepo  coursewareAppravolRepo;
	@Autowired
	private CoursewareShareRepo coursewareShareRepo;
	@Autowired
	private OfficeFileConvertor officeFileConvertor;
	
	
	@GetMapping(value = "share/list/{courseId}")
	public Mono<Page<Courseware>> shareList(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "1000", required = false) Integer size,
			@PathVariable Long unitId) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

		return Mono.just(coursewareRepo.findAllByUnitId(pageable, unitId));

	}
	@DeleteMapping(value = "share/delete/{id}")
	public Mono<String> shareDel(@PathVariable Long id) {
		coursewareRepo.deleteById(id);

		return Mono.empty();
	}
	
	@PostMapping(value = "unit/{unitId}")
	public Mono<Void> shareCreate(@RequestBody User user,
			@RequestParam(value = "role",  required = true) String role) {
 
		return Mono.empty();
	}
	
	
	@GetMapping(value = "appravol/list/{courseId}")
	public Mono<Page<Courseware>> appravolList(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "1000", required = false) Integer size,
			@PathVariable Long unitId) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

		return Mono.just(coursewareRepo.findAllByUnitId(pageable, unitId));

	}
	@DeleteMapping(value = "appravol/delete/{id}")
	public Mono<String> appravolDel(@PathVariable Long id) {
		coursewareRepo.deleteById(id);

		return Mono.empty();
	}
	
	@PostMapping(value = "appravol/{unitId}")
	public Mono<Void> appravolCreate(@RequestBody User user,
			@RequestParam(value = "role",  required = true) String role) {
 
		return Mono.empty();
	}
	
	
	@GetMapping(value = "unit/{unitId}")
	public Mono<Page<Courseware>> list(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "1000", required = false) Integer size,
			@PathVariable Long unitId) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "createdDate");

		return Mono.just(coursewareRepo.findAllByUnitId(pageable, unitId));

	}
	@DeleteMapping(value = "delete/{id}")
	public Mono<String> del(@PathVariable Long id) {
		coursewareRepo.deleteById(id);

		return Mono.empty();
	}
	
	
	@Value("${files.uploadpath}")
	private String uploadpath;

	@Value("${files.docspath}")
	private String docspath;

	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<String> requestBodyFlux(@RequestPart("file") FilePart filePart,
			@RequestPart("courseId") String courseId, @RequestPart("unitId") String unitId) throws Exception {
		Path p = Paths.get(uploadpath, courseId, unitId);
		Path p2 = Paths.get(docspath, courseId, unitId);
		if (!Files.exists(p)) {
			Files.createDirectories(p);
		}
		;
		if (!Files.exists(p2)) {
			Files.createDirectories(p2);
		}
		;
		String extendName = getFileExtendName(filePart.filename());
		Path tempFile = Files.createTempFile(p, "", extendName);
		Path tempFile2 = Files.createTempFile(p2, "", extendName + ".html");
	
		filePart.transferTo(tempFile.toFile());
		Courseware cw = new Courseware();
		cw.setCourseId(Long.valueOf(courseId));
		cw.setFilename(filePart.filename());
		cw.setUnitId(Long.valueOf(unitId));
		cw.setFilepath(courseId + "/" + unitId + "/" + tempFile.getFileName());
		cw.setFiletype(extendName);
		
		if (filePart.filename().endsWith(".doc") ||filePart.filename().endsWith(".docx") ) {

			officeFileConvertor.convertWordToHtml(tempFile.toAbsolutePath().toString(),
					tempFile2.toAbsolutePath().toString());
			//cw.setMediaType(MediaType.);
		}else if (filePart.filename().endsWith(".xls") ||filePart.filename().endsWith(".xlsx") ) {
			officeFileConvertor.convertExcelToHtml(tempFile.toAbsolutePath().toString(),
					tempFile2.toAbsolutePath().toString());

		}else if (filePart.filename().endsWith(".ppt") ||filePart.filename().endsWith(".pptx") ) {
			tempFile2 = Files.createTempFile(p2, "", extendName + ".pdf");
			officeFileConvertor.convertPowerpointToPdf(tempFile.toAbsolutePath().toString(),
					tempFile2.toAbsolutePath().toString());
		}else {
			tempFile2 = Files.createTempFile(p2, "", extendName);
			
			Files.delete(tempFile2);
			FileSystemUtils.copyRecursively(tempFile, tempFile2 );
		}

		cw.setUrl(courseId + "/" + unitId + "/" + tempFile2.getFileName());
		coursewareRepo.save(cw);

		System.out.println(tempFile.toString());
		return Mono.just(filePart.filename());
	}

 

	private String getFileExtendName(String fileName) {
		if (fileName.lastIndexOf(".") > 0) {
			return fileName.substring(fileName.lastIndexOf("."));

		} else {
			return "";
		}
	}
}
