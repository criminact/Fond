package com.noobcode.fond.Model;

import java.util.ArrayList;
import java.util.List;

public class Match {
    List<String> liked;
    List<String> likedBy;
    List<String> matches = new ArrayList<>();

    public Match(List<String> liked, List<String> likedBy) {
        this.liked = liked;
        this.likedBy = likedBy;
    }

    public List<String> getMatches(){
        if(liked != null && likedBy != null){
            for(String id : liked){
                if(likedBy.contains(id)){
                    matches.add(id);
                }
            }
            return matches;
        }else{
            return null;
        }
    }

}
