package com.leemin.genealogy.control;

import com.leemin.genealogy.config.ErrorKey;
import com.leemin.genealogy.model.*;
import com.leemin.genealogy.service.GenealogyService;
import com.leemin.genealogy.service.PedigreeService;
import com.leemin.genealogy.service.PeopleService;
import com.leemin.genealogy.service.StorageService;
import com.leemin.genealogy.upload.PeopleUpload;
import com.leemin.genealogy.upload.StatusUpload;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.function.BiConsumer;

@Controller
public class PedigreeController {

    @Autowired
    PedigreeService pedigreeService;

    @Autowired
    GenealogyService genealogyService;
    @Autowired
    StorageService storageService;

    @Autowired
    PeopleService peopleService;

    private final Logger logger = LoggerFactory.getLogger(RestUploadController.class);

//
//    @GetMapping("/pedigree/{idGenealogy}")
//    public ModelAndView home(@PathVariable(name = "idGenealogy")long idGenealogy){
//        ModelAndView mv = new ModelAndView("/genealogy/pedigree");
//        mv.addObject("idGenealogy",idGenealogy);
//        return mv;
//    }


    @RequestMapping(value = "/genealogy/{idGenealogy}/pedigree/add", method = RequestMethod.GET)
    public ModelAndView viewList(
            Principal principal,
            @PathVariable(name = "idGenealogy")Long idGenealogy) {
        ModelAndView mv = new ModelAndView("/genealogy/pedigree-add");
        PedigreeModel pedigreeModel = new PedigreeModel();

        pedigreeModel.setName("Nguyễn Tộc");
        pedigreeModel.setHistory("Đây là dòng tộc nguyễn");
        mv.addObject("pedigree",pedigreeModel);
        mv.addObject("idGenealogy",idGenealogy);
        return mv;
    }

    @RequestMapping(value = "/genealogy/{idGenealogy}/pedigree/add", method = RequestMethod.POST)
    public ModelAndView createGenealogy(
            Principal principal,
            @Valid
            @ModelAttribute(value = "pedigree")
                    PedigreeModel pedigreeModel,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            BindingResult bindingResult,
            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/genealogy/pedigree-detail");
        GenealogyModel genealogyModel = genealogyService.find(idGenealogy, principal.getName());
        PedigreeModel add = pedigreeService.add(pedigreeModel, idGenealogy, principal.getName());

        mv.addObject("genealogy",genealogyModel);
        mv.addObject("pedigree",add);
        return mv;
    }


    @RequestMapping(value = "/genealogy/{idGenealogy}/pedigree/{idPedigree}/edit", method = RequestMethod.POST )
    public ModelAndView updatePedigree(
            Principal principal,
            @PathVariable(value = "idPedigree")
                    long idPedigree,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            @Valid
            @ModelAttribute(value = "pedigree")
                    PedigreeModel pedigreeModel,
            BindingResult bindingResult,
            HttpServletRequest request) {
        pedigreeModel.setId(idPedigree);
        pedigreeModel = pedigreeService.update(pedigreeModel);
        GenealogyModel genealogyModel = genealogyService.find(idGenealogy, principal.getName());
        ModelAndView mv = new ModelAndView("/genealogy/pedigree-detail");
        mv.addObject("genealogy",genealogyModel);
        mv.addObject("pedigree",pedigreeModel);
        return mv;
    }

    @RequestMapping(value = "/genealogy/{idGenealogy}/pedigree/{idPedigree}/edit", method = RequestMethod.GET )
    public ModelAndView editPedigree(
            Principal principal,
            @PathVariable(value = "idPedigree")
                    long idPedigree,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/genealogy/pedigree-edit");
        GenealogyModel genealogyModel = genealogyService.find(idGenealogy, principal.getName());
        PedigreeModel pedigreeModel = pedigreeService.findByIdPedigreeModel(idPedigree);

        mv.addObject("genealogy",genealogyModel);
        mv.addObject("pedigree",pedigreeModel);
        return mv;
    }


    @RequestMapping(value = "/genealogy/{idGenealogy}/pedigree/{idPedigree}/detail", method = RequestMethod.GET)
    public ModelAndView detail(
            Principal principal,
            @PathVariable(value = "idPedigree")
                    long idPedigree,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/genealogy/pedigree-detail");
        GenealogyModel genealogyModel = genealogyService.find(idGenealogy, principal.getName());

        PedigreeModel pedigree = pedigreeService.findByIdPedigreeModel(idPedigree);

        mv.addObject("genealogy",genealogyModel);
        mv.addObject("pedigree",pedigree);
        return mv;
    }


    @RequestMapping(value = "/genealogy/{idGenealogy}/pedigree/{idPedigree}/import", method = RequestMethod.GET)
    public ModelAndView importListPeople(
            Principal principal,
            @PathVariable(value = "idPedigree")
                    long idPedigree,
            @PathVariable(name = "idGenealogy")
                    long idGenealogy,
            HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("/genealogy/pedigree-import");
//        GenealogyModel genealogyModel = genealogyService.find(idGenealogy, principal.getName());
//        PedigreeModel pedigree = pedigreeService.findByIdPedigreeModel(idPedigree);
        mv.addObject("idPedigree",idPedigree);
        mv.addObject("idGenealogy",idGenealogy);
        return mv;
    }

    @RequestMapping(value = "/genealogy/{idGenealogy}/pedigree/{idPedigree}/import", method = RequestMethod.POST)
    public ResponseEntity<?> uploadListPeople(
            @RequestParam(value = "idGenealogy",required = false,defaultValue = "") long idGenealogy,
            @RequestParam(value = "idPedigree",required = false,defaultValue = "") long idPedigree,
            @RequestParam("files") MultipartFile uploadfiles) {
        logger.debug("Multiple file upload!");

//        // Get file name
//        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
//                                        .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        String uploadedFileName = uploadfiles.getOriginalFilename();
        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity(ErrorKey.ERROR_EMPTY, HttpStatus.OK);
        }
        try {

            String fileName = idGenealogy + idPedigree + System.currentTimeMillis() + "update" + getExtensionsFile(uploadedFileName);

            saveUploadedFiles(uploadfiles, fileName);
            TreeMap<Long, Set<PeopleUpload>> integerSetHashMap = readExcelFile(fileName);
            List<PeopleModel> result = saveData(integerSetHashMap, idPedigree);
            return new ResponseEntity<>(result , HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
//        return new ResponseEntity("Successfully uploaded - "
//                                          + uploadedFileName, HttpStatus.OK);

    }


    private String getExtensionsFile(String uploadedFileName) {
        return uploadedFileName.substring(uploadedFileName.lastIndexOf("."));
    }

    private List<PeopleModel> saveData(TreeMap<Long, Set<PeopleUpload>> integerSetHashMap, long idPedigree) {
        //find key not is parent or contain in database but not contain in list
        PedigreeModel pedigree = pedigreeService.findByIdPedigreeModel(idPedigree);
        HashMap<Long,Long> containerRealParentKey = new HashMap<>();
        List<PeopleModel> result = new ArrayList<>();
        if(pedigree == null) return null;

        //remove all people in pedigree before upload
        peopleService.removeAllByIdPedigree(pedigree);

        for(Map.Entry<Long, Set<PeopleUpload>> entry : integerSetHashMap.entrySet()) {

            //get real key for parent
            Long key = entry.getKey();
            Long realKey = containerRealParentKey.get(key);
            if(realKey != null) key = realKey;

            //upload child of parent
            Set<PeopleUpload> value = entry.getValue();
            for (PeopleUpload people : value) {
                PeopleModel peopleSave = new PeopleModel();
                long idParent = people.isRealParent()? people.getIdParent():key;
                if(idParent != -1){
                    PeopleModel parent = peopleService.findById(key);
                    if (parent == null) {
                        people.setStatusUpload(StatusUpload.CANT_FIND_PARENT);
                        peopleSave.setLifeIndex(0);
                        continue;
                    }
                    peopleSave.setLifeIndex(parent.getLifeIndex()+1);
                    peopleSave.setParent(parent);
                    peopleSave.setParentKey(parent.getParentKey() + "_" + parent.getId());
                }else{
                    peopleSave.setParentKey("r");
                }
                peopleSave.setPedigree(pedigree);
                peopleSave.setName(people.getName());
                peopleSave.setNickName(people.getNickName());
                peopleSave.setGender(people.getGender());
                peopleSave.setBirthday(people.getBirthday());
                peopleSave.setDeadDay(people.getDeadDay());
                peopleSave.setAddress(people.getAddress());
                peopleSave.setDegree(people.getDegree());
                peopleSave.setChildIndex(people.getChildIndex());
//                peopleSave.setImg(people.getImg());
                peopleSave.setImg("/dist/img/user2-160x160.jpg");
                peopleSave.setDes(people.getDes());
                peopleSave.setDataExtra(people.getDataExtra());
                PeopleModel add = peopleService.add(peopleSave);
                if (add != null) {
                    people.setStatusUpload(StatusUpload.SUCCESS);
                    containerRealParentKey.put(people.getId(),add.getId());
                    result.add(add);
//                    Set<PeopleUpload> peopleUploads = integerSetHashMap.get(key);
//                    integerSetHashMap.remove(key);
//                    integerSetHashMap.put(add.getId(), null);
                }
                else {
                    people.setStatusUpload(StatusUpload.FAIL);
                }
            }
            // do what you have to do here
            // In your case, another loop.
        }
        return result;
    }

    private void saveUploadedFiles(MultipartFile file,String fileName) throws IOException {
            if (file.isEmpty()) {
                return;
            }
            storageService.store(file,fileName);
    }

    private final Path rootLocation = Paths.get("upload-dir");

    private TreeMap<Long,Set<PeopleUpload>> readExcelFile(String fileName){
        TreeMap<Long,Set<PeopleUpload>> containerPeople = new TreeMap<>();
        try {

            FileInputStream excelFile = new FileInputStream(this.rootLocation.resolve(fileName).toFile());
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();
            System.out.println("total row" + datatypeSheet.getLastRowNum());
            iterator.next();
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                PeopleUpload peopleModel =  readPeopleFromRow(currentRow);
                long  idParent = peopleModel.getIdParent();
                boolean isRealParent = peopleModel.isRealParent();
                long key = (isRealParent ? -2 : idParent);
                Set<PeopleUpload> peopleModels = containerPeople.get(idParent);
                if(peopleModels == null) {
                    peopleModels = new HashSet<>();
                }
                peopleModels.add(peopleModel);
                containerPeople.put(idParent, peopleModels);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return containerPeople;
    }

    private PeopleUpload readPeopleFromRow(Row currentRow) {

        PeopleUpload peopleModel = new PeopleUpload();
        peopleModel.setIdParent( - 1);

        Iterator<Cell> cellIterator = currentRow.iterator();
        int indexCell = 0;
        while (cellIterator.hasNext()) {
            Cell currentCell = cellIterator.next();
            if(currentCell.getCellTypeEnum() != CellType._NONE){
                UploadFormat col = UploadFormat.values()[currentCell.getColumnIndex()];
                switch (col){
                    case STT:
                        peopleModel.setId((long) currentCell.getNumericCellValue());
                        break;
                    case STT_CHA:
                        CellType cellTypeEnum = currentCell.getCellTypeEnum();
                        if(cellTypeEnum == CellType.STRING){
                            String value = currentCell.getStringCellValue();
                            if(value.isEmpty()){
                                peopleModel.setIdParent(-1);
                            }else
                            if(value.startsWith("#")){
                                peopleModel.setRealParent(true);
                                peopleModel.setIdParent(Long.parseLong(value.substring(value.lastIndexOf("#")+1)));
                            }else{
                                peopleModel.setIdParent(Long.parseLong(value));
                            }
                        }else if(cellTypeEnum == CellType.NUMERIC){
                            peopleModel.setIdParent((long) currentCell.getNumericCellValue());
                        }
                        break;
                    case DOI:
                        peopleModel.setLifeIndex((int) currentCell.getNumericCellValue());
                        break;
                    case MOI_QUAN_HE:
                        break;
                    case HO_VA_TEN:
                        peopleModel.setName(currentCell.getStringCellValue());
                        break;
                    case TEN_THUONG_GOI:
                        peopleModel.setNickName(currentCell.getStringCellValue());
                        break;
                    case GIOI_TINH:
                        String gender = currentCell.getStringCellValue();
                        gender = gender.trim().toLowerCase();
                        if(gender.equalsIgnoreCase("nam") || gender.equalsIgnoreCase("male")){
                            peopleModel.setGender(1);
                        }else if(gender.equalsIgnoreCase("nữ") || gender.equalsIgnoreCase("female")){
                                peopleModel.setGender(0);
                        }else{
                            peopleModel.setGender(-1);
                        }
                        break;
                    case CON_THU:
                        peopleModel.setChildIndex((int) currentCell.getNumericCellValue());
                        break;
                    case NGAY_SINH:
                        peopleModel.setBirthday(currentCell.getDateCellValue());
                        break;
                    case NGAY_MAT:
                        peopleModel.setDeadDay(currentCell.getDateCellValue());
                        break;
                    case DIA_CHI:
                        peopleModel.setAddress(currentCell.getStringCellValue());
                        break;
                    case TRINH_DO_HOC_VAN:
                        peopleModel.setDegree(currentCell.getStringCellValue());
                        break;
                    case LINK_HINH_ANH:
                        peopleModel.setImg(currentCell.getStringCellValue());
                        break;
                    case MO_TA:
                        peopleModel.setDes(currentCell.getStringCellValue());
                        break;
                    case SU_TICH_CAU_NOI:
                        peopleModel.setDataExtra(currentCell.getStringCellValue());
                        break;
                }
            }else {
                System.out.print("NONE cELL");
            }


            //getCellTypeEnum shown as deprecated for version 3.15
            //getCellTypeEnum ill be renamed to getCellType starting from version 4.0
            if (currentCell.getCellTypeEnum() == CellType.STRING) {
                System.out.print(currentCell.getStringCellValue() + "--");
            } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
                System.out.print(currentCell.getNumericCellValue() + "--");
            }
        }
        System.out.println();
        return peopleModel;
    }

}

enum UploadFormat {
    STT,
    STT_CHA,
    DOI,
    MOI_QUAN_HE,
    HO_VA_TEN,
    TEN_THUONG_GOI,
    GIOI_TINH,
    CON_THU,
    NGAY_SINH,
    NGAY_MAT,
    DIA_CHI,
    TRINH_DO_HOC_VAN,
    LINK_HINH_ANH,
    MO_TA,
    SU_TICH_CAU_NOI
}