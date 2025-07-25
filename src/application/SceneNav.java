/*
 * Copyright (c) 2023 Richard Kelly Wiles (rkwiles@twc.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *  Created on: Aug 6, 2023
 *      Author: Kelly Wiles
 */

package application;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;

public class SceneNav {

//	private CtGlobal cg = CtGlobal.getInstance();
	
	/**
     * Convenience constants for fxml layouts managed by the navigator.
     */
	public static final String MAIN    			= "screens/SceneNav.fxml";
	public static final String DFC		  		= "screens/Dfc.fxml";
	public static final String MANGEDIRS  		= "screens/ManageDirs.fxml";
	public static final String MANAGEIGNORES  	= "screens/ManageIgnores.fxml";
	public static final String SETTINGS		  	= "screens/Settings.fxml";
	public static final String VIEWREPORT	  	= "screens/ViewReport.fxml";
    
    /** The main application layout controller. */
    private SceneNavController mainController = null;
    public Stack<String> sceneQue = null;
    private boolean kbReturn = false;
    
    public Map<String, SceneInfo> fxmls = new HashMap<String, SceneInfo>();
    
    public SceneNav() {
    	
    }
    
    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public void setMainController(SceneNavController mainController) {
    	this.sceneQue = new Stack<String>();
        this.mainController = mainController;
    }
    
    public SceneInfo getSceneInfo(String sceneName) {
    	if (this.fxmls != null)
    		return fxmls.get(sceneName);
    	else
    		return null;
    }
    
    /**
     * Loads the vista specified by the fxml file into the
     * vistaHolder pane of the main application layout.
     *
     * Previously loaded vista for the same fxml file are not cached.
     * The fxml is loaded anew and a new vista node hierarchy generated
     * every time this method is invoked.
     *
     * A more sophisticated load function could potentially add some
     * enhancements or optimizations, for example:
     *   cache FXMLLoaders
     *   cache loaded vista nodes, so they can be recalled or reused
     *   allow a user to specify vista node reuse or new creation
     *   allow back and forward history like a browser
     *
     * @param fxml the fxml file to be loaded.
     */
    public boolean loadScene(String fxml) {

    	kbReturn = false;
    	
    	if (this.fxmls != null && this.fxmls.containsKey(fxml)) {
    		SceneInfo si = fxmls.get(fxml);
    		si.startTime = new Date().getTime();
    		mainController.setSceneNav(si.node);
    		if (si.controller instanceof RefreshScene) {
	    		RefreshScene c = (RefreshScene)si.controller;
	    		c.refreshScene();
    		}
    	} else {
	        try {
	        	SceneInfo si = new SceneInfo();
	        	FXMLLoader loader = new FXMLLoader(SceneNav.class.getResource(fxml));
	        	if (loader != null) {
		        	si.loader = loader;
		        	si.node = (Node)loader.load();			// must be called before you call getController
		        	si.controller = loader.getController();
		        	si.startTime = new Date().getTime();
		        	
		        	fxmls.put(fxml, si);
		            mainController.setSceneNav(si.node);
		            
		            if (si.controller instanceof RefreshScene) {
			    		RefreshScene c = (RefreshScene)si.controller;
			    		c.refreshScene();
		    		}
	        	}
	        } catch (IOException e) {
	        	System.out.println("Error loading FXML: " + fxml);
	            e.printStackTrace();
	            return true;
	        }
    	}
    	
    	pushStack(fxml);
    	
    	return false;
    }
    
    public void scenePop() {
		
		if (this.sceneQue != null && this.sceneQue.empty() == false) {
			String s = this.sceneQue.pop();
			SceneInfo si = this.getSceneInfo(s);
			this.fxmls.put(s,  si);
			
			mainController.setSceneNav(si.node);
    		if (si.controller instanceof RefreshScene) {
	    		RefreshScene c = (RefreshScene)si.controller;
	    		c.leaveScene();
    		}
			
			if (this.sceneQue.empty() == false) {
				this.loadScene(this.sceneQue.peek());
			} else {
				this.loadScene(SceneNav.DFC);
			}
		}
	}
    
    public String getCurrentScene() {
    	return this.sceneQue.peek();
    }
    
    private void pushStack(String fxml) {
    	if (this.sceneQue.size() > 0) {
    		if (this.sceneQue.peek().equals(fxml) == false)
    			this.sceneQue.push(fxml);
    	} else {
    		this.sceneQue.push(fxml);
    	}
    	
//    	System.out.println("STACK: " + ag.sceneQue);
    }
    
//    public void clickButton(String txt) {
//    	String fxml = this.sceneQue.peek();		// get current scene.
//    	
//    	SceneInfo si = this.fxmls.get(fxml);
//		mainController.setSceneNav(si.node);
//		if (si.controller instanceof RefreshScene) {
//    		RefreshScene c = (RefreshScene)si.controller;
//    		c.clickIt(txt, WidgetType.BUTTON);
//		}
//    }
//    
//    public void clickRegion(String txt) {
//    	String fxml = this.sceneQue.peek();		// get current scene.
//    	
//    	SceneInfo si = this.fxmls.get(fxml);
//		mainController.setSceneNav(si.node);
//		if (si.controller instanceof RefreshScene) {
//    		RefreshScene c = (RefreshScene)si.controller;
//    		c.clickIt(txt, WidgetType.REGION);
//		}
//    }
//    
//    public void clickCheckbox(String txt) {
//    	String fxml = this.sceneQue.peek();		// get current scene.
//    	
//    	SceneInfo si = fxmls.get(fxml);
//		mainController.setSceneNav(si.node);
//		if (si.controller instanceof RefreshScene) {
//    		RefreshScene c = (RefreshScene)si.controller;
//    		c.clickIt(txt, WidgetType.CHECKBOX);
//		}
//    }
    
    public void gotoMainMenu() {
    	if (this.sceneQue != null && this.sceneQue.empty() == false) {
    		while(true) {
    			String fxml = this.sceneQue.peek();
    			if (fxml.equals(SceneNav.DFC) == true)
    				break;
    			
    			SceneInfo si = fxmls.get(fxml);
        		mainController.setSceneNav(si.node);
        		if (si.controller instanceof RefreshScene) {
    	    		RefreshScene c = (RefreshScene)si.controller;
    	    		c.leaveScene();
        		}
    		}
    	}
    }
    
    public boolean isKbReturn() {
    	return kbReturn;
    }
    
}
