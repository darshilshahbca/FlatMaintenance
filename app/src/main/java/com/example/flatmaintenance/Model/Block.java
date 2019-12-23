package com.example.flatmaintenance.Model;

public class Block {

    String blockId, blockName, owerName,ownerContact, renterName, renterContact;
    double maint_amount;
    Boolean inUse, onRent;

    public Block() {
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public Block(String blockName, Boolean inUse) {
        this.blockName = blockName;
        this.inUse = inUse;
    }

    public Block(String blockName, String owerName, String ownerContact, double maint_amount, Boolean inUse, Boolean onRent) {
        this.blockName = blockName;
        this.owerName = owerName;
        this.ownerContact = ownerContact;
        this.maint_amount = maint_amount;
        this.inUse = inUse;
        this.onRent = onRent;
    }

    public Block(String blockName, String owerName, String ownerContact, String renterName, String renterContact, double maint_amount, Boolean inUse, Boolean onRent) {
        this.blockName = blockName;
        this.owerName = owerName;
        this.ownerContact = ownerContact;
        this.renterName = renterName;
        this.renterContact = renterContact;
        this.maint_amount = maint_amount;
        this.inUse = inUse;
        this.onRent = onRent;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getOwerName() {
        return owerName;
    }

    public void setOwerName(String owerName) {
        this.owerName = owerName;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public String getRenterContact() {
        return renterContact;
    }

    public void setRenterContact(String renterContact) {
        this.renterContact = renterContact;
    }

    public double getMaint_amount() {
        return maint_amount;
    }

    public void setMaint_amount(double maint_amount) {
        this.maint_amount = maint_amount;
    }

    public Boolean getInUse() {
        return inUse;
    }

    public void setInUse(Boolean inUse) {
        this.inUse = inUse;
    }

    public Boolean getOnRent() {
        return onRent;
    }

    public void setOnRent(Boolean onRent) {
        this.onRent = onRent;
    }
}
