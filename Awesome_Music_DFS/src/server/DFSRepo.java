package server;

import chord.DFS;

public class DFSRepo {
	
	private static DFS dfs;
	private static DFSRepo dfsRepo;
	
	private DFSRepo() {

	}
	
	public static DFSRepo getInstance() {
		if(dfsRepo == null) {
			dfsRepo = new DFSRepo();
		}
		return dfsRepo;
	}
	
	public void setDFS(DFS dfs) {
		this.dfs = dfs;
	}
	
	public static DFS getDFS() {
		return dfs;
	}

}
