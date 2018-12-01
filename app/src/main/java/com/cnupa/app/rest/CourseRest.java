package com.cnupa.app.rest;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.cnupa.app.file.OfficeFileConvertor;
import com.cnupa.app.model.Course;
import com.cnupa.app.model.Courseware;
import com.cnupa.app.model.TRS;
import com.cnupa.app.model.User;
import com.cnupa.app.model.enums.CourseType;
import com.cnupa.app.repo.CourseRepo;
import com.cnupa.app.repo.CoursewareRepo;
import com.cnupa.app.repo.TRSRepo;
import com.cnupa.app.repo.UserRepo;
import com.cnupa.app.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@RestController
@Transactional
@RequestMapping("course")
@Slf4j
public class CourseRest {

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private CourseRepo courseRepo;

	@Autowired
	private TRSRepo trsRepo;
	@Autowired
	private CoursewareRepo coursewareRepo;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private OfficeFileConvertor officeFileConvertor;
	@Value("${files.uploadpath}")
	private String uploadpath;

	@Value("${files.docspath}")
	private String docspath;

	/**
	 * 创建课程
	 * 
	 * @param course
	 * @param token
	 * @return
	 */
	@PostMapping(value = "create/team/{trsId}")
	public Mono<Void> createPublic(@RequestBody Course course, @PathVariable Long trsId) {

		course.setType(CourseType.TEAM);
		courseRepo.save(course);
		TRS trs = trsRepo.getOne(trsId);
		trs.getCourses().add(course);
		trsRepo.save(trs);
		return Mono.empty();

	}

	@PostMapping(value = "create/personal")
	public Mono<Void> createPrivate(@RequestBody Course course,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
		Long uid=jwtUtil.getUserId(token);
		User u =userRepo.getOne(uid);
		course.setType(CourseType.PERSONAL);
		course.setChief(u);
		courseRepo.save(course);
		return Mono.empty();

	}
	@PostMapping(value = "join/{id}")
	public Mono<Void> join(@PathVariable Long id,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = true) String token) {
		Long uid=jwtUtil.getUserId(token);
		User u =userRepo.getOne(uid);
		Course c=courseRepo.getOne(id);
		
		c.getMembers().add(u);
		

		courseRepo.save(c);
		return Mono.empty();

	}


	@PutMapping(value = "update/{id}")
	public Mono<ResponseEntity<String>> update(@PathVariable Long id,@RequestBody Map course) {
		
		
		Course src=courseRepo.getOne(id);
		if(course.get("opened")!=null) {
			src.setOpened(Boolean.valueOf(course.get("opened").toString() ));
		}
		if(course.get("allowPastView")!=null) {
			src.setAllowPastView(Boolean.valueOf(course.get("allowPastView").toString() ));
		}

		courseRepo.save(src);

		return Mono.empty();

	}

//	@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
	@DeleteMapping(value = "delete/{id}")
	public Mono<String> del(@PathVariable Long id) {
		courseRepo.deleteById(id);

		return Mono.empty();
	}

	@GetMapping(value = "{id}")
	public Mono<Course> get(@PathVariable("id") Long id) {
		System.out.println("get");
		return Mono.just(courseRepo.findById(id).get());

	}

	@GetMapping(value = "list")
	public Mono<Page<Course>> list(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

		return Mono.just(courseRepo.findAll(pageable));

	}
	@GetMapping(value = "list/team")
	public Mono<Page<Course>> listTeam(@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

		return Mono.just(courseRepo.findAllByType(pageable,CourseType.TEAM));

	}

	@GetMapping(value = "current/{type}")
	public Mono<Page<Course>> current(@PathVariable("type") String type,
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,

			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
		Long userId = jwtUtil.getUserId(token);
		return Mono.just(courseRepo.findAllByTypeAndChiefId(pageable, CourseType.valueOf(type.toUpperCase()), userId));

	}

	@GetMapping(value = "current/{type}/member")
	public Mono<Page<Course>> currentMember(@PathVariable("type") String type,
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,

			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
		Long userId = jwtUtil.getUserId(token);
		return Mono.just(courseRepo.findAllByTypeAndMembersId(pageable, CourseType.valueOf(type.toUpperCase()), userId));

	}
	
	@GetMapping(value = "list/{type}")
	public Mono<Page<Course>> listByType(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@PathVariable String type,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

		return Mono.just(courseRepo.findAllByType(pageable, CourseType.valueOf(type.toUpperCase())));


	}
	@GetMapping(value = "list/{type}/opened")
	public Mono<Page<Course>> listopened(
			@RequestParam(value = "page", defaultValue = "0", required = false) Integer page,
			@RequestParam(value = "size", defaultValue = "100", required = false) Integer size,
			@PathVariable String type,
			@RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
		Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");

		return Mono.just(courseRepo.findAllByTypeAndOpenedTrue(pageable, CourseType.valueOf(type.toUpperCase())));


	}
	@PostMapping(value = "/upload/{courseId}/{prop}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Mono<String> requestBodyFlux(@RequestPart("file") FilePart filePart, @PathVariable Long courseId,
			@PathVariable String prop) throws Exception {

		String path = getConvertFilePath(filePart, courseId);
		if (path == null)
			throw new Exception("invaild file");
		Course c = courseRepo.getOne(courseId);
		Courseware cw = new Courseware();
		cw.setCourseId(Long.valueOf(courseId));
		cw.setFilename(filePart.filename());
		cw.setFiletype(getFileExtendName(filePart.filename()));
		cw.setUrl(courseId + "/" + path);
		switch (prop) {
		case "intro":

			coursewareRepo.save(cw);
			c.setIntro(cw);
			courseRepo.save(c);
			break;
		case "outline":
			coursewareRepo.save(cw);
			c.setOutline(cw);
			courseRepo.save(c);
			break;
		case "team":
			coursewareRepo.save(cw);
			c.setTeam(cw);
			courseRepo.save(c);
			break;
		case "bannerImg":
			c.setBannerImg(courseId + "/" +path);
			courseRepo.save(c);
			break;
		case "iconImg":
			c.setIconImg(courseId + "/" +path);
			courseRepo.save(c);
			break;
		default:
			System.out.println("default");
			break;
		}

		return Mono.just(filePart.filename());
	}

	private String getConvertFilePath(FilePart filePart, Long courseId) throws Exception {
		String extendName = getFileExtendName(filePart.filename());
		Path p = Paths.get(uploadpath, courseId.toString());
		Path p2 = Paths.get(docspath, courseId.toString());
		if (!Files.exists(p)) {
			Files.createDirectories(p);
		}
		;
		if (!Files.exists(p2)) {
			Files.createDirectories(p2);
		}
		Path tempFile = Files.createTempFile(p, "", extendName);
		filePart.transferTo(tempFile.toFile());
		Path tempFile2;
		if (filePart.filename().endsWith(".doc") || filePart.filename().endsWith(".docx")) {

			tempFile2 = Files.createTempFile(p2, "", extendName + ".html");
			officeFileConvertor.convertWordToHtml(tempFile.toAbsolutePath().toString(),
					tempFile2.toAbsolutePath().toString());
			return tempFile2.getFileName().toString();

		} else if (filePart.filename().endsWith(".png") || filePart.filename().endsWith(".jpeg") || filePart.filename().endsWith(".jpg")) {
			tempFile2 = Files.createTempFile(p2, "", extendName);
		
			Files.delete(tempFile2);
			FileSystemUtils.copyRecursively(tempFile, tempFile2 );
		
			return tempFile2.getFileName().toString();
		}
		return null;
	}

	private String getFileExtendName(String fileName) {
		if (fileName.lastIndexOf(".") > 0) {
			return fileName.substring(fileName.lastIndexOf("."));

		} else {
			return "";
		}
	}
}
