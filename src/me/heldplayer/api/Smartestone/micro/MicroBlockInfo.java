
package me.heldplayer.api.Smartestone.micro;

public class MicroBlockInfo {

    private IMicroBlockMaterial material;
    private IMicroBlockSubBlock subBlock;
    private int data;

    public MicroBlockInfo(IMicroBlockMaterial material, IMicroBlockSubBlock subBlock, int data) {
        this.material = material;
        this.subBlock = subBlock;
        this.data = data;
    }

    public IMicroBlockMaterial getMaterial() {
        return this.material;
    }

    public IMicroBlockSubBlock getType() {
        return this.subBlock;
    }

    public int getData() {
        return this.data;
    }

    public void setData(int data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + data;
        result = prime * result + ((material == null) ? 0 : material.hashCode());
        result = prime * result + ((subBlock == null) ? 0 : subBlock.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MicroBlockInfo other = (MicroBlockInfo) obj;
        if (data != other.data)
            return false;
        if (material == null) {
            if (other.material != null)
                return false;
        }
        else if (!material.equals(other.material))
            return false;
        if (subBlock == null) {
            if (other.subBlock != null)
                return false;
        }
        else if (!subBlock.equals(other.subBlock))
            return false;
        return true;
    }

}
