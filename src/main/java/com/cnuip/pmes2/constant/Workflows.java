package com.cnuip.pmes2.constant;

import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;

/**
 * Workflows
 *
 * @author: xiongwei
 * Date: 2018/1/28 下午2:54
 */
public abstract class Workflows {
	
	public static Map<Integer,String> processNameMap;
	
	public static Map<Integer,String> processTypeMap;
	
	public static Map<Integer,String> taskKeyMap;
	
	static {
		processNameMap = new HashedMap<>();
		processTypeMap = new HashedMap<>();
		taskKeyMap = new HashedMap<>();
	}

    /**
     *
     * @param taskKey
     * @return
     */
    public static Integer findTaskByTaskKey(ProcessType type, String taskKey) {
        return null;
    }
    
    public static String findTaskKeyById(int id) {
    	String name = Workflows.taskKeyMap.get(id);
    	if(name!=null) {
    		return name;
    	}
    	Workflows.TaskType[] taskTypes = Workflows.TaskType.values();
    	for (TaskType taskType : taskTypes) {
    		if(taskType.getId() == id) {
    			Workflows.taskKeyMap.put(id, taskType.getKey());
    			return taskType.getKey();
    		}
    	}
    	return null;
    }
    
    public static String findProcessTypeById(int id) {
    	String name = Workflows.processTypeMap.get(id);
    	if(name!=null) {
    		return name;
    	}
    	Workflows.ProcessType[] processTypes = Workflows.ProcessType.values();
    	for (ProcessType processType : processTypes) {
			if(processType.getId() == id) {
				Workflows.processTypeMap.put(id, processType.getProcessDefinitionKey());
				return processType.getProcessDefinitionKey();
			}
		}
    	return null;
    	
    }
    
    public static String findProcessNameById(int id) {
    	String name = Workflows.processNameMap.get(id);
    	if(name!=null) {
    		return name;
    	}
    	Workflows.ProcessType[] processTypes = Workflows.ProcessType.values();
    	for (ProcessType processType : processTypes) {
			if(processType.getId() == id) {
				Workflows.processNameMap.put(id, processType.getName());
				return processType.getName();
			}
		}
    	return null;
    }
    
    public static Long findNextTaskId(Integer processType, Long taskId) {
    	switch (processType) {
		case 2:
			Workflows.PatentBasicIndex[] patentBasicIndexs = Workflows.PatentBasicIndex.values();
			for (PatentBasicIndex patentBasicIndex : patentBasicIndexs) {
				if(patentBasicIndex.getTaskId().equals(taskId)) {
					return patentBasicIndex.getNextTaskId();
				}
			}
			break;
		case 3:
			Workflows.PatentValueIndex[] patentValueIndexs = Workflows.PatentValueIndex.values();
			for (PatentValueIndex patentBasicIndex : patentValueIndexs) {
				if(patentBasicIndex.getTaskId().equals(taskId)) {
					return patentBasicIndex.getNextTaskId();
				}
			}
			break;
		case 4:
			Workflows.PatentPriceIndex[] patentPriceIndexs = Workflows.PatentPriceIndex.values();
			for (PatentPriceIndex patentBasicIndex : patentPriceIndexs) {
				if(patentBasicIndex.getTaskId().equals(taskId)) {
					return patentBasicIndex.getNextTaskId();
				}
			}
			break;
		case 5:
			Workflows.PatentDeepIndex[] patentDeepIndexs = Workflows.PatentDeepIndex.values();
			for (PatentDeepIndex patentBasicIndex : patentDeepIndexs) {
				if(patentBasicIndex.getTaskId().equals(taskId)) {
					return patentBasicIndex.getNextTaskId();
				}
			}
			break;
		case 6:
			Workflows.EnterPriseIndex[] enterpriseIndexs = Workflows.EnterPriseIndex.values();
			for (EnterPriseIndex patentBasicIndex : enterpriseIndexs) {
				if(patentBasicIndex.getTaskId().equals(taskId)) {
					return patentBasicIndex.getNextTaskId();
				}
			}
			break;
		case 7:
			Workflows.EnterPriseRequirementIndex[] enterPriseRequirementIndex = Workflows.EnterPriseRequirementIndex.values();
			for (EnterPriseRequirementIndex patentBasicIndex : enterPriseRequirementIndex) {
				if(patentBasicIndex.getTaskId().equals(taskId)) {
					return patentBasicIndex.getNextTaskId();
				}
			}
			break;
		case 8:
			Workflows.EnterpriseRequireMatch[] enterpriseRequireMatch = Workflows.EnterpriseRequireMatch.values();
			for (EnterpriseRequireMatch patentBasicIndex : enterpriseRequireMatch) {
				if(patentBasicIndex.getTaskId().equals(taskId)) {
					return patentBasicIndex.getNextTaskId();
				}
			}
			break;
		default:
			break;
		}
    	return null;
    }
    
    public enum TaskType {
    	AssignOrder("派单", 1, "AssignOrder"),
        AutoIndex("自动标引", 2, "AutoIndex"),
        ManualIndex("人工标引", 3, "ManualIndex"),
        ManualIndexAudit("人工标引审核", 4, "ManualIndexAudit"),
        ValueIndex("价值评估", 5, "ValueIndex"),
        PriceIndex("价格评估", 6, "PriceIndex"),
        DeepIndex("深度标引", 7, "DeepIndex"),
        SemiAutoIndex("半自动标引", 8, "SemiAutoIndex"),
        ArtificialSelection("匹配结果人工筛选", 9, "ArtificialSelection"),
        SemiAutoIndexAudit("半自动标引审核", 10, "SemiAutoIndexAudit"),
        ;
    	
    	private TaskType(String name, Integer id, String key) {
			this.name = name;
			this.id = id;
			this.key = key;
		}
    	
		private String name;
    	private Integer id;
    	private String key;
    	
		public String getName() {
			return name;
		}
		
		public Integer getId() {
			return id;
		}

		public String getKey() {
			return key;
		}
    	
    }

    /**
     * 实例类型
     */
    public enum InstanceType {

        Patent(1), // 专利实例
        EnterpriseInfo(2), // 企业信息实例
        EnterpriseRequire(3), // 企业需求实例
        Expert(4), // 专家实例
    	MATCH(5); //匹配实例

        private Integer value;

        InstanceType(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }

    /**
     * 流程类型
     */
    public enum ProcessType {

        BatchUpdate("batchUpdate", 1, "专利批量处理"), // 专利批量处理
        PatentBasicIndex("patentBasicIndex", 2, "专利基础索引"), // 专利基础索引
    	PatentValueIndex("patentValueIndex", 3, "专利价值标引"), // 专利价值标引
    	PatentPriceIndex("patentPriceIndex", 4, "专利价格标引"), // 专利价格标引
    	PatentDeepIndex("patentDeepIndex", 5, "专利深度标引"), // 专利深度标引
    	EnterPriseIndex("enterpriseInfoIndex", 6, "企业信息标引"), // 企业信息标引
    	EnterPriseRequirementIndex("enterpriseRequireIndex", 7, "企业需求标引"), //企业需求标引
    	EnterpriseRequireMatch("enterpriseRequireMatch", 8, "企业需求匹配"), //企业需求匹配
    	;

        private String processDefinitionKey;
        private Integer id;
        private String name;

        ProcessType(String processDefinitionKey, Integer id , String name) {
            this.processDefinitionKey = processDefinitionKey;
            this.id = id;
            this.name = name;
        }

        public String getProcessDefinitionKey() {
            return this.processDefinitionKey;
        }

        public Integer getId() {
            return id;
        }

		public String getName() {
			return name;
		}
    }

    /**
     * 基础标引
     */
    public enum PatentBasicIndex {

        AssignOrder("sendOrder1", 1L, 2L),
        AutoIndex("autoIndex", 2L, 3L),
        AssignOrder2("sendOrder2", 3L, 41L),
        SemiAutoIndex("semiAutoIndex", 41L, 80L),
        AssignOrder5("sendOrder26", 80L, 79L),
        SemiAutoIndexAudit("semiAutoIndexAudit", 79L, 5L),
        AssignOrder3("sendOrder3", 5L, 4L),
        ManualIndex("humanIndex", 4L, 42L),
        AssignOrder4("sendOrder3", 42L, 6L),
        ManualIndexAudit("humanIndexAudit", 6L , null);

        private String taskKey;
        private Long taskId;
        private Long nextTaskId;
        PatentBasicIndex(String taskKey, Long taskId, Long nextTaskId) {
            this.taskKey = taskKey;
            this.taskId = taskId;
            this.nextTaskId = nextTaskId;
        }

        public String getTaskKey() {
            return this.taskKey;
        }

        public Long getTaskId() {
            return taskId;
        }

		public Long getNextTaskId() {
			return nextTaskId;
		}
    }

    /**
     * 价值标引
     */
    public enum PatentValueIndex {

        AssignOrder("sendOrder5", 15L, 16L),
        AutoIndex("autoIndex2", 16L, 17L),
        AssignOrder2("sendOrder6", 17L, 43L),
        SemiAutoIndex("semiAutoIndex2", 43L, 82L),
        AssignOrder6("sendOrder27", 82L, 81L),
        SemiAutoIndexAudit("semiAutoIndexAudit2", 81L, 44L),
        AssignOrder3("sendOrder7", 44L, 18L),
        ManualIndex("humanIndex2", 18L, 19L),
        AssignOrder4("sendOrder8", 19L, 20L),
        ManualIndexAudit("humanIndexAudit2", 20L , 45L),
    	AssignOrder5("sendOrder9", 45L, 46L),
    	ValueIndex("valueIndex", 46L, null),
    	;

        private String taskKey;
        private Long taskId;
        private Long nextTaskId;
        PatentValueIndex(String taskKey, Long taskId, Long nextTaskId) {
            this.taskKey = taskKey;
            this.taskId = taskId;
            this.nextTaskId = nextTaskId;
        }

        public String getTaskKey() {
            return this.taskKey;
        }

        public Long getTaskId() {
            return taskId;
        }

		public Long getNextTaskId() {
			return nextTaskId;
		}
    }

    /**
     * 价格
     */
    public enum PatentPriceIndex {

        AssignOrder("sendOrder10", 47L, 48L),
        AutoIndex("autoIndex3", 48L, 49L),
        AssignOrder2("sendOrder11", 49L, 50L),
        SemiAutoIndex("semiAutoIndex3", 50L, 84L),
        AssignOrder6("sendOrder28", 84L, 83L),
        SemiAutoIndexAudit("semiAutoIndexAudit3", 83L, 51L),
        AssignOrder3("sendOrder12", 51L, 52L),
        ManualIndex("humanIndex3", 52L, 53L),
        AssignOrder4("sendOrder13", 53L, 54L),
        ManualIndexAudit("humanIndexAudit3", 54L , 55L),
    	AssignOrder5("sendOrder14", 55L, 56L),
    	PriceIndex("priceIndex", 56L, null),
    	;

        private String taskKey;
        private Long taskId;
        private Long nextTaskId;
        PatentPriceIndex(String taskKey, Long taskId, Long nextTaskId) {
            this.taskKey = taskKey;
            this.taskId = taskId;
            this.nextTaskId = nextTaskId;
        }

        public String getTaskKey() {
            return this.taskKey;
        }

        public Long getTaskId() {
            return taskId;
        }

		public Long getNextTaskId() {
			return nextTaskId;
		}
    }

    /**
     * 深度标引
     */
    public enum PatentDeepIndex {

        AssignOrder("sendOrder15", 57L, 58L),
        AutoIndex("autoIndex4", 58L, 59L),
        AssignOrder2("sendOrder16", 59L, 60L),
        SemiAutoIndex("semiAutoIndex4", 60L, 86L),
        AssignOrder5("sendOrder29", 86L, 85L),
        SemiAutoIndexAudit("semiAutoIndexAudit4", 85L, 61L),
        AssignOrder3("sendOrder17", 61L, 62L),
        ManualIndex("humanIndex4", 62L, 63L),
        AssignOrder4("sendOrder18", 63L, 64L),
        ManualIndexAudit("humanIndexAudit4", 64L , null),
    	;

        private String taskKey;
        private Long taskId;
        private Long nextTaskId;
        PatentDeepIndex(String taskKey, Long taskId, Long nextTaskId) {
            this.taskKey = taskKey;
            this.taskId = taskId;
            this.nextTaskId = nextTaskId;
        }

        public String getTaskKey() {
            return this.taskKey;
        }

        public Long getTaskId() {
            return taskId;
        }

		public Long getNextTaskId() {
			return nextTaskId;
		}
    }
    
    /**
     * 企业信息标引
     */
    public enum EnterPriseIndex {
    	
    	AssignOrder("sendOrder19", 65L, 66L),
    	AutoIndex("autoIndex5", 66L, 67L),
    	AssignOrder2("sendOrder20", 67L, 68L),
    	ManualIndex("humanIndex5", 68L, 69L),
    	AssignOrder3("sendOrder21", 69L, 70L),
    	ManualIndexAudit("humanIndexAudit5", 70L , null),
    	;
    	
    	private String taskKey;
    	private Long taskId;
    	private Long nextTaskId;
    	EnterPriseIndex(String taskKey, Long taskId, Long nextTaskId) {
    		this.taskKey = taskKey;
    		this.taskId = taskId;
    		this.nextTaskId = nextTaskId;
    	}
    	
    	public String getTaskKey() {
    		return this.taskKey;
    	}
    	
    	public Long getTaskId() {
    		return taskId;
    	}
    	
    	public Long getNextTaskId() {
    		return nextTaskId;
    	}
    }
    
    /**
     * 企业需求标引
     */
    public enum EnterPriseRequirementIndex {
    	
    	AssignOrder("sendOrder22", 71L, 72L),
    	AutoIndex("autoIndex6", 72L, 73L),
    	AssignOrder2("sendOrder23", 73L, 74L),
    	ManualIndex("humanIndex6", 74L, 75L),
    	AssignOrder3("sendOrder24", 75L, 76L),
    	ManualIndexAudit("humanIndexAudit6", 76L , null),
    	;
    	
    	private String taskKey;
    	private Long taskId;
    	private Long nextTaskId;
    	EnterPriseRequirementIndex(String taskKey, Long taskId, Long nextTaskId) {
    		this.taskKey = taskKey;
    		this.taskId = taskId;
    		this.nextTaskId = nextTaskId;
    	}
    	
    	public String getTaskKey() {
    		return this.taskKey;
    	}
    	
    	public Long getTaskId() {
    		return taskId;
    	}
    	
    	public Long getNextTaskId() {
    		return nextTaskId;
    	}
    }
    
    /**
     * 企业需求匹配
     */
    public enum EnterpriseRequireMatch {
    	
    	AssignOrder("sendOrder25", 77L, 78L),
    	ArtificialSelection("artificialSelection", 78L , null),
    	;
    	
    	private String taskKey;
    	private Long taskId;
    	private Long nextTaskId;
    	EnterpriseRequireMatch(String taskKey, Long taskId, Long nextTaskId) {
    		this.taskKey = taskKey;
    		this.taskId = taskId;
    		this.nextTaskId = nextTaskId;
    	}
    	
    	public String getTaskKey() {
    		return this.taskKey;
    	}
    	
    	public Long getTaskId() {
    		return taskId;
    	}
    	
    	public Long getNextTaskId() {
    		return nextTaskId;
    	}
    }
    
}
