public File updatePestWeeklyReport(String oldFileDirpath, String newFilepath) throws IOException, InvalidFormatException {
		
		//map to store the new data 
		//district mandal crop pest areas
		TreeMap<String, TreeMap<String, TreeMap<String, TreeMap<String, Map<List<Double>, List<String>>>>>> data = new TreeMap<>();
		Map<String, Map<String, Map<String, Double>>> naPestData = new HashMap<>();
		
		//reading the new file
		FileInputStream newFileTS = new FileInputStream(new File(newFilepath));
		XSSFWorkbook newWorkBook = new XSSFWorkbook (newFileTS);
		XSSFSheet newSheet = newWorkBook.getSheetAt(0);
		
		Iterator<Row> ite = newSheet.rowIterator();
		ite.next();
		ite.next();
		
		while(ite.hasNext()) {
			Row row = ite.next();
			Iterator<Cell> cite= row.cellIterator();
			
			String districtName = cite.next().toString().toUpperCase();
			String mandalName = cite.next().toString().toUpperCase();
			String cropName = cite.next().toString().toUpperCase();
			String pestName = cite.next().toString().toUpperCase();
			Double actualArea = Double.parseDouble(cite.next().toString());
			Double impactedArea = Double.parseDouble(cite.next().toString());
			
			List<Double> areas = new ArrayList<>();
			areas.add(actualArea);
			areas.add(impactedArea);
			
			
			
			if(pestName.compareTo("NA") == 0) {
			
//				if(naPestData.get(districtName)!=null) {
//					
//					if(naPestData.get(districtName).get(mandalName)!=null) {
//						naPestData.get(districtName).get(mandalName).put(cropName, actualArea);
//					}
//					else {
//						
//						Map<String, Double> cropNameAreaMap = new HashMap<>();
//						cropNameAreaMap.put(cropName, actualArea);
//						naPestData.get(districtName).put(mandalName, cropNameAreaMap);
//					}
//					
//				}
//				else {
//					Map<String, Double> cropNameAreaMap = new HashMap<>();
//					cropNameAreaMap.put(cropName, actualArea);
//					Map<String, Map<String, Double>> mandalCropNameAreaMap = new HashMap<>();
//					mandalCropNameAreaMap.put(mandalName, cropNameAreaMap);
//					naPestData.put(districtName, mandalCropNameAreaMap);
//				}
				continue;
				
			}
			
			//filling the map
			if(data.get(districtName)!=null) {
				
				if(data.get(districtName).get(mandalName)!=null) {
					
					if(data.get(districtName).get(mandalName).get(cropName)!=null) {

						Map<List<Double>, List<String>> areaResultMap = new HashMap<>();
						areaResultMap.put(areas,null);
						data.get(districtName).get(mandalName).get(cropName).put(pestName, areaResultMap);
						
					}
					else {
						
						TreeMap<String,Map<List<Double>, List<String>>> datasub1 = new TreeMap<>();
						Map<List<Double>, List<String>> areaResultMap = new HashMap<>();
						areaResultMap.put(areas,null);
						datasub1.put(pestName, areaResultMap);
						
						data.get(districtName).get(mandalName).put(cropName, datasub1);
						
					}
					
				}
				else {
					
					TreeMap<String, TreeMap<String, Map<List<Double>, List<String>>>> datasub1 = new TreeMap<>();
					TreeMap<String, Map<List<Double>, List<String>>> datasub2 = new TreeMap<>();
					Map<List<Double>, List<String>> areaResultMap = new HashMap<>();
					areaResultMap.put(areas,null);
					datasub2.put(pestName, areaResultMap);
					datasub1.put(cropName, datasub2);
					data.get(districtName).put(mandalName, datasub1);
					
				}
				
			}
			else {
				
				TreeMap<String, TreeMap<String, TreeMap<String, Map<List<Double>, List<String>>>>> datasub1 = new TreeMap<>();
				TreeMap<String, TreeMap<String, Map<List<Double>, List<String>>>> datasub2 = new TreeMap<>();
				TreeMap<String, Map<List<Double>, List<String>>> datasub3 = new TreeMap<>();
				Map<List<Double>, List<String>> areaResultMap = new HashMap<>();
				areaResultMap.put(areas,null);
				datasub3.put(pestName, areaResultMap);
				datasub2.put(cropName, datasub3);
				datasub1.put(mandalName, datasub2);
				data.put(districtName, datasub1);
				
			}
		}
		
		
		//getting all the files in the new directory
		File folder = new File(oldFileDirpath);
		File[] listOfFiles = folder.listFiles();
		
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    	
		    	String filedistrictName = file.getName();
		    	
		    	FileInputStream oldFileIS = new FileInputStream(new File(file.getAbsolutePath()));
		    	XSSFWorkbook oldWorkBook = new XSSFWorkbook (oldFileIS);
		    	XSSFSheet oldSheet = oldWorkBook.getSheetAt(0);
		    	
		    	String filePath = file.getAbsolutePath();
		    
		    	//map to store the old data of a particular district
		    	TreeMap<String, TreeMap<String, TreeMap<String, Map<List<Double>,List<String>>>>> oldData = new TreeMap<>();
		    	
		    	Iterator<Row> oldite = oldSheet.rowIterator();
				Row beforeDeletingSheetHeading = oldite.next();
				
				//getting the sheet heading of the old file
				String sheetHeading = beforeDeletingSheetHeading.getCell(0).toString();
				
				Row rowheader = oldite.next();
				Iterator<Cell> headercite= rowheader.cellIterator();
				List<String> headers = new ArrayList<>();
				
				//getting the headers of the old file
				while(headercite.hasNext()) {
					headers.add(headercite.next().toString());
				}
				
		    	while(oldite.hasNext()) {
		    		
		    		Row row = oldite.next();
					Iterator<Cell> cite= row.cellIterator();
					cite.next();
					cite.next();
					String mandalName = cite.next().toString().toUpperCase();
					String cropName = cite.next().toString().toUpperCase();
					String pestName = cite.next().toString().toUpperCase();
					Double actualArea = Double.parseDouble(cite.next().toString());
					Double normalArea = Double.parseDouble(cite.next().toString());
					
					List<String> results = new ArrayList<>();
					while(cite.hasNext()) {
						results.add(cite.next().toString());
					}
					List<Double> areas = new ArrayList<>();
					areas.add(actualArea);
					areas.add(normalArea);
					
					if(oldData.get(mandalName)!=null) {
						
						if(oldData.get(mandalName).get(cropName)!=null) {
							
							Map<List<Double>, List<String>> areaReportDataMap = new HashMap<>();
							areaReportDataMap.put(areas, results);
							oldData.get(mandalName).get(cropName).put(pestName, areaReportDataMap);
							
						}
						else {
							
							TreeMap<String, Map<List<Double>, List<String>>>dataSub1 = new TreeMap<>();
							Map<List<Double>, List<String>> areaReportDataMap = new HashMap<>();
							areaReportDataMap.put(areas, results);
							dataSub1.put(pestName, areaReportDataMap);
							oldData.get(mandalName).put(cropName,dataSub1);
							
						}
						
					}
					else {
						
						Map<List<Double>, List<String>> areaReportDataMap = new HashMap<>();
						areaReportDataMap.put(areas, results);
						TreeMap<String, Map<List<Double>, List<String>>>dataSub1 = new TreeMap<>();
						TreeMap<String, TreeMap<String, Map<List<Double>, List<String>>>> dataSub2 = new TreeMap<>();
						dataSub1.put(pestName, areaReportDataMap);
						dataSub2.put(cropName, dataSub1);
						oldData.put(mandalName,dataSub2);
						
					}
		    	
		    	}
		    	
		    	
		    	String districtName = filedistrictName.toUpperCase().substring(0, filedistrictName.indexOf('.'));
		    	TreeMap<String, TreeMap<String, TreeMap<String, Map<List<Double>, List<String>>>>> mandalWizeData = data.get(filedistrictName.toUpperCase().substring(0, filedistrictName.indexOf('.')));
//		    	Map<String, Map<String, Double>> mandalWiseNAPestData = naPestData.get(filedistrictName.toUpperCase().substring(0, filedistrictName.indexOf('.')));
		    	
		    	
		    	for(Map.Entry<String, TreeMap<String, TreeMap<String, Map<List<Double>, List<String>>>>> mandalDataEntry : mandalWizeData.entrySet()) {
		    		
		    		
			    	
		    		if(mandalDataEntry.getValue() == null) {
		    			continue;
		    		}
		    		String mandalName = mandalDataEntry.getKey();
		    		
		    		if(oldData.get(mandalName)==null) {
		    			
		    			oldData.put(mandalName, mandalDataEntry.getValue());
		    		}
		    		
		    		for(Map.Entry<String, TreeMap<String, Map<List<Double>, List<String>>>> cropDataEntry : mandalDataEntry.getValue().entrySet()) {
		    		
		    			if(cropDataEntry.getValue() == null) {
		    				continue;
		    			}
		    			
		    			String cropName = cropDataEntry.getKey();
		    			
		    			if(oldData.get(mandalName).get(cropName) == null) {
		    				oldData.get(mandalName).put(cropName, cropDataEntry.getValue());
		    			}
		    			
		    			for(Map.Entry<String, Map<List<Double>, List<String>>> pestDataEntry : cropDataEntry.getValue().entrySet()) {
		    				
//		    				if(oldData.get(mandalName).get(cropName).get(pestDataEntry.getKey()) == null) {
//		    					TreeMap<String, Map<List<Double>, List<String>>> tempMap = new TreeMap<>();
//		    					tempMap.put(pestDataEntry.getKey(), pestDataEntry.getValue());
//		    					oldData.get(mandalName).put(cropName, tempMap);
//		    				}
//		    				
		    				if(oldData.get(mandalName).get(cropName).get(pestDataEntry.getKey()) == null) {
		    					oldData.get(mandalName).get(cropName).put(pestDataEntry.getKey(),pestDataEntry.getValue());
		    				}
		    				
		    				Map<List<Double>, List<String>> areaResultMap = new HashMap<>();
		    				for(Map.Entry<List<Double>, List<String>> entry : pestDataEntry.getValue().entrySet()) {
		    					for(Map.Entry<List<Double>, List<String>> entry2 : oldData.get(mandalName).get(cropName).get(pestDataEntry.getKey()).entrySet()) {
		    						areaResultMap.put(entry.getKey(),entry2.getValue());
		    					}
		    				}
		    				
	    					oldData.get(mandalName).get(cropName).put(pestDataEntry.getKey(), areaResultMap);
		    			}
		    			
		    		}
		    		
		    	}
		    	
		    	
//		    	for(Map.Entry<String, Map<String, Double>> naPestEntry : mandalWiseNAPestData.entrySet()) {
//		    		
//		    		if(oldData.get(naPestEntry.getKey())==null) {
//		    			continue;
//		    		}
//		    		
//		    		for(Map.Entry<String, Double> cropAreaEntry : naPestEntry.getValue().entrySet()) {
//		    			
//		    			if(oldData.get(naPestEntry.getKey()).get(cropAreaEntry.getKey())==null) {
//		    				continue;
//		    			}
//		    			TreeMap<String, Map<List<Double>,List<String>>> naPestEntryCorrespondingOldMapEntry = new TreeMap<>();
//		    			TreeMap<String, Map<List<Double>,List<String>>> oldDataEntry = oldData.get(naPestEntry.getKey()).get(cropAreaEntry.getKey());
//		    			
//		    			List<Double> newActialArea= new ArrayList<>();
//		    			
//		    			newActialArea.add(cropAreaEntry.getValue());
////		    			newActialArea.add(oldData.);
//		    			oldData.get(naPestEntry.getKey()).get(cropAreaEntry.getKey());
//		    		}
//		    	}
		    	
		    	System.out.println(oldData);
		    	
		    	oldFileIS.close();
		    	file.delete();
		    	
		    
				XSSFWorkbook workbook = new XSSFWorkbook (); 
				XSSFSheet sheet = workbook.createSheet("Sheet");
				Row sheetheading = sheet.createRow(0);
				sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));
				
				sheetheading.createCell(0).setCellValue(sheetHeading);
				
				XSSFRow row1 = sheet.createRow(1);
				int countheader = 0;
				for(String header :headers) {
					XSSFCell rc6 = row1.createCell(countheader);
					sheet.autoSizeColumn(countheader++);
					rc6.setCellValue(header);
				}
				
				int rowNumber = 2;
				
		    	//updating the new sheet 
		    	int count =1;
		    	for(Map.Entry<String, TreeMap<String, TreeMap<String, Map<List<Double>, List<String>>>>> mandalDataEntry : oldData.entrySet()) {
		    		
		    		
		    		if(mandalDataEntry.getValue() == null ) {
		    			continue;
		    		}
		    		
		    		for(Map.Entry<String, TreeMap<String, Map<List<Double>, List<String>>>> cropDataEntry : mandalDataEntry.getValue().entrySet()) {
		    			
		    			if(cropDataEntry.getValue() == null) {
		    				continue;
		    			}
		    			
		    			for(Map.Entry<String, Map<List<Double>, List<String>>> pestDataEntry : cropDataEntry.getValue().entrySet()) {
		    				
		    				XSSFRow row = sheet.createRow(rowNumber++);
		    				XSSFCell rc6 = row.createCell(0);
		    				rc6.setCellValue(Integer.toString(count++));
		    				
		    				XSSFCell rc7 = row.createCell(1);
		    				rc7.setCellValue(filedistrictName.toUpperCase().substring(0, filedistrictName.indexOf('.')));
		    				
		    				XSSFCell rc1 = row.createCell(2);
		    				rc1.setCellValue(mandalDataEntry.getKey());
		    				
		    				XSSFCell rc2 = row.createCell(3);
		    				rc2.setCellValue(cropDataEntry.getKey());
		    				
		    				XSSFCell rc5 = row.createCell(4);
		    				rc5.setCellValue(pestDataEntry.getKey());
		    				
		    				List<Double> areas = new ArrayList<>();
		    				List<String> results = new ArrayList<>();
		    				
		    				for(Map.Entry<List<Double>, List<String>> entry : pestDataEntry.getValue().entrySet()) {
		    					areas.addAll(entry.getKey());
		    					if(entry.getValue()!=null)
		    						results.addAll(entry.getValue());
		    				}
		    				
		    				XSSFCell rc3 = row.createCell(5);
		    				rc3.setCellValue(areas.get(0));
		    				
		    				XSSFCell rc4 = row.createCell(6);
		    				rc4.setCellValue(areas.get(1));
		    				
		    				int count2 =7;
		    				for(String result : results) {
		    					XSSFCell rcCount = row.createCell(count2++);
			    				rcCount.setCellValue(result);
		    				}
		    				
		    				FileOutputStream fos =new FileOutputStream(new File(filePath));
		    		        workbook.write(fos);
		    		        fos.close();
		    			}
		    			
		    		}
		    		
		    	}
		    	
		    	
		        System.out.println("DONE");
		    }
		    
		}
		
		return	null;	
	}