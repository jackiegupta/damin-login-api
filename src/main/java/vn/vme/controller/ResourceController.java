package vn.vme.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.apache.tika.metadata.Property.ValueType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.jayway.jsonpath.internal.Utils;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import vn.vme.common.JConstants;
import vn.vme.common.JConstants.CityList;
import vn.vme.common.JConstants.CurrencyType;
import vn.vme.common.JConstants.GameType;
import vn.vme.common.JConstants.NewsType;
import vn.vme.common.JConstants.NotifyStatus;
import vn.vme.common.JConstants.NotifyType;
import vn.vme.common.JConstants.PeriodType;
import vn.vme.common.JConstants.PoolType;
import vn.vme.common.JConstants.Roles;
import vn.vme.common.JConstants.Scope;
import vn.vme.common.JConstants.Status;
import vn.vme.common.JConstants.TourStatus;
import vn.vme.common.JConstants.TourType;
import vn.vme.common.JConstants.TransactionStatus;
import vn.vme.common.JConstants.TransactionType;
import vn.vme.common.JConstants.UserLevel;
import vn.vme.common.JConstants.UserStatus;
import vn.vme.common.JConstants.UserType;
import vn.vme.common.JConstants.Vip;
import vn.vme.common.URI;
import vn.vme.io.VO;
import vn.vme.io.user.UserVO;
import vn.vme.model.Response;
import vn.vme.service.StorageService;

@Controller
@RequestMapping(URI.V1 + URI.RESOURCE)
public class ResourceController extends BaseController {

    static Logger log = LoggerFactory.getLogger(ResourceController.class.getName());
    @Autowired(required = false)
    public StorageService storageService;

    @RequestMapping(path = URI.APP_DOWNLOAD, method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadAppFile(
            @ApiParam(defaultValue = "download.jpg") @PathVariable String filename) {

        Resource resource = storageService.loadAsResource("app/"+ filename);

        try {
			return ResponseEntity.ok()
			        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
			        .contentLength(resource.contentLength())
			        .contentType(MediaType.APPLICATION_OCTET_STREAM)
			        .body(resource);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return ResponseEntity.ok().body(null);
    }
    @RequestMapping(path = URI.DOWNLOAD, method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(
    		@ApiParam(defaultValue = "download.jpg") @PathVariable String filename) {
    	
    	Resource resource = storageService.loadAsResource(filename);
    	
    	try {
    		return ResponseEntity.ok()
    				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
    				.contentLength(resource.contentLength())
    				.contentType(MediaType.APPLICATION_OCTET_STREAM)
    				.body(resource);
    	} catch (IOException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	return ResponseEntity.ok().body(null);
    }

   
    @GetMapping(URI.IMAGE + "/**")
    public ResponseEntity<byte[]> image(HttpServletResponse response,HttpServletRequest req,
    		 @ApiParam(defaultValue = "image.jpg") @PathVariable String filename) throws Exception {
       filename= req.getRequestURI().replace(URI.V1 + URI.RESOURCE + "/image/", "");
       Path path = storageService.load(filename);
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(path.toFile());
        } catch (FileNotFoundException e) {
            log.warn(e.getMessage());
            try {
                path = storageService.load("image.jpg");
                inputStream = new FileInputStream(path.toFile());
            } catch (Exception ex) {
                log.warn(ex.getMessage());
            }
        }
        if (inputStream != null) {
            byte[] byteArray = IOUtils.toByteArray(inputStream);

            String mimeType = new Tika().detect(path.toFile());
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(mimeType)).body(byteArray);
        } else {
            return ResponseEntity.ok().body(null);
        }
    }

    @ApiOperation(value = "Get constants by param or all (type = not required)")
    @ApiResponses({@ApiResponse(code = 200, message = "OK, Return list value response  ", response = UserVO.class),
            @ApiResponse(code = 422, response = Response.class, message = "Invalid data"),
            @ApiResponse(code = 500, response = Response.class, message = "Internal server error")})
    @CrossOrigin(origins = "*")
    @GetMapping(value = URI.LIST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getConstantsList(@RequestParam(name = "type", required = false) String type)
            throws Exception {
        log.info("Get ConstantsList by type  " + type);
        List<VO> constantList = null;
        Map<String, List<VO>> map = new HashMap<>();

        List enumList = Arrays.asList(JConstants.class.getDeclaredClasses());
        int index =0;
        for (Object object : enumList) {
            constantList = new ArrayList<>();
            String key = object.toString().replace("class " + JConstants.class.getName() + "$", "");
            if(key.equals(type)) {
            	//return response(enumList.get(index));
            	 //constantList.add(new VO(value.name(), Status.valueOf(value.name())//.value));
            }
            index++;
        }


        // Status
        constantList = new ArrayList<>();
        for (Status value : Status.values()) {
            constantList.add(new VO(value.name(), Status.valueOf(value.name()).value));
        }
        map.put(Status.class.getSimpleName(), constantList);
        
       
        
        //TransactionType
        constantList = new ArrayList<>();
        for (TransactionType value : TransactionType.values()) {
            constantList.add(new VO(value.name(), TransactionType.valueOf(value.name()).value));
        }
        map.put(TransactionType.class.getSimpleName(), constantList);


        // TransactionStatus
        constantList = new ArrayList<>();
        for (TransactionStatus value : TransactionStatus.values()) {
            constantList.add(new VO(value.name(), TransactionStatus.valueOf(value.name()).value));
        }
        map.put(TransactionStatus.class.getSimpleName(), constantList);

        // UserStatus
        constantList = new ArrayList<>();
        for (UserStatus value : UserStatus.values()) {
            constantList.add(new VO(value.name(), UserStatus.valueOf(value.name()).value));
        }
        map.put(UserStatus.class.getSimpleName(), constantList);
        
        // UserType
        constantList = new ArrayList<>();
        for (UserType value : UserType.values()) {
            constantList.add(new VO(value.name(), UserType.valueOf(value.name()).value));
        }
        map.put(UserType.class.getSimpleName(), constantList);
        
        //NotifyStatus
        constantList = new ArrayList<>();
        for (NotifyStatus value : NotifyStatus.values()) {
            constantList.add(new VO(value.name(), NotifyStatus.valueOf(value.name()).value));
        }
        map.put(NotifyStatus.class.getSimpleName(), constantList);
        
        //NotifyType
        constantList = new ArrayList<>();
        for (NotifyType value : NotifyType.values()) {
            constantList.add(new VO(value.name(), NotifyType.valueOf(value.name()).value));
        }
        map.put(NotifyType.class.getSimpleName(), constantList);
       
        
        // UserRoles
        constantList = new ArrayList<>();
        for (Roles value : Roles.values()) {
        	constantList.add(new VO(value.name(), value.name()));
        }
        map.put(Roles.class.getSimpleName(), constantList);

        
        //PatternInput
        constantList = new ArrayList<>();
        
        //PatternType
        constantList = new ArrayList<>();
        for (Scope value : Scope.values()) {
        	constantList.add(new VO(value.name(), value.name()));
        }
        map.put(Scope.class.getSimpleName(), constantList);
        
        //ValueType
        constantList = new ArrayList<>();
        for (ValueType value : ValueType.values()) {
        	constantList.add(new VO(value.name(), value.name()));
        }
        map.put(ValueType.class.getSimpleName(), constantList);
        
        //GameType
        constantList = new ArrayList<>();
        for (GameType value : GameType.values()) {
        	constantList.add(new VO(value.name(), value.value));
        }
        map.put(GameType.class.getSimpleName(), constantList);
        
        //GifcodeType
        constantList = new ArrayList<>();
        for (PoolType value : PoolType.values()) {
        	constantList.add(new VO(value.name(), value.value));
        }
        map.put(PoolType.class.getSimpleName(), constantList);
        
        //NewsType
        constantList = new ArrayList<>();
        for (NewsType value : NewsType.values()) {
        	constantList.add(new VO(value.name(), value.value));
        }
        map.put(NewsType.class.getSimpleName(), constantList);
        
        
        //UserLevel
        constantList = new ArrayList<>();
        for (UserLevel value : UserLevel.values()) {
        	constantList.add(new VO(value.name(), value.value));
        }
        map.put(UserLevel.class.getSimpleName(), constantList);
        
        //Vip
        constantList = new ArrayList<>();
        for (Vip value : Vip.values()) {
        	constantList.add(new VO(value.name(), value.value));
        }
        map.put(Vip.class.getSimpleName(), constantList);
        
        //CurrencyType
        constantList = new ArrayList<>();
        for (CurrencyType value : CurrencyType.values()) {
        	constantList.add(new VO(value.name(), value.name()));
        }
        map.put(CurrencyType.class.getSimpleName(), constantList);
        
        
        //CityList
        constantList = new ArrayList<>();
        for (CityList value : CityList.values()) {
        	constantList.add(new VO(value.name(), value.name()));
        }
        map.put(CityList.class.getSimpleName(), constantList);
        
        
      
        
        //PeriodType
        constantList = new ArrayList<>();
        for (PeriodType value : PeriodType.values()) {
        	constantList.add(new VO(value.name(), value.name()));
        }
        map.put(PeriodType.class.getSimpleName(), constantList);
       
        //TourStatus
        constantList = new ArrayList<>();
        for (TourStatus value : TourStatus.values()) {
        	constantList.add(new VO(value.name(), value.value));
        }
        map.put(TourStatus.class.getSimpleName(), constantList);
        
        //TourType
        constantList = new ArrayList<>();
        for (TourType value : TourType.values()) {
        	constantList.add(new VO(value.name(), value.value));
        }
        map.put(TourType.class.getSimpleName(), constantList);
        
        
        
        // RESPOSNE value map
        if (Utils.isEmpty(type)) {
            return response(map);
        } else {
            List<VO> data = map.get(type);
            if (data == null) {
                data = new ArrayList<>();
            }
            return response(data);
        }
    }
}
