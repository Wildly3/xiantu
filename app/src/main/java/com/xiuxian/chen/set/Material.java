package com.xiuxian.chen.set;

//材料
public class Material extends Item implements Cloneable
{
	public Material(){
		//super.id=(cu_id++);
		super.kind=KIND_MATERIAL;
	}

    @Override
    protected Material clone() throws CloneNotSupportedException {
        return (Material)super.clone();
    }

    public Material NewObject(){
		Material material = null;
        try {
            material = this.clone();
            material.explain = this.explain;
        } catch (CloneNotSupportedException e) {
        }
		return material;
	}
	
}
