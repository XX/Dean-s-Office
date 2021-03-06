package ru.sgu.csit.inoc.deansoffice.domain;

import com.google.common.base.Objects;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by XX (MesheryakovAV)
 * Date: 07.09.2010
 * Time: 9:18:30
 */
@Entity
public class Reference extends Document {
    private ReferenceType type;
    private ReferenceState state;
    private Date registeredDate;
    private Date issuedDate;
    private String purpose;
    private Long ownerId;

    public ReferenceType getType() {
        return type;
    }

    public void setType(final ReferenceType type) {
        this.type = type;
    }

    public ReferenceState getState() {
        return state;
    }

    public void setState(final ReferenceState state) {
        this.state = state;
    }

    public Date getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(final Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(final Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(final String purpose) {
        this.purpose = purpose;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(final Long ownerId) {
        this.ownerId = ownerId;
    }

    public static enum ReferenceType {
        REFERENCE_1, REFERENCE_2, REFERENCE_3
    }

    public static enum ReferenceState {
        REGISTERED, PROCESSED, READY, ISSUED
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Reference that = (Reference) o;

        return super.equals(that) &&
                Objects.equal(this.type, that.type) &&
                Objects.equal(this.state, that.state) &&
                Objects.equal(this.registeredDate, that.registeredDate) &&
                Objects.equal(this.issuedDate, that.issuedDate) &&
                Objects.equal(this.purpose, that.purpose) &&
                Objects.equal(this.ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(
                super.hashCode(),
                type,
                state,
                registeredDate,
                issuedDate,
                purpose,
                ownerId);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .addValue(super.toString())
                .add("type", type)
                .add("state", state)
                .add("registeredDate", registeredDate)
                .add("issuedDate", issuedDate)
                .add("purpose", purpose)
                .add("ownerId", ownerId)
                .toString();
    }

    public static Reference make(
            final Long id,
            final String number,
            final Date signedDate,
            final Template printTemplate,
            final ReferenceType type,
            final ReferenceState state,
            final Date registeredDate,
            final Date issuedDate,
            final String purpose,
            final Long ownerId) {

        final Reference reference = new Reference();
        reference.setId(id);
        reference.setNumber(number);
        reference.setSignedDate(signedDate);
        reference.setPrintTemplate(printTemplate);
        reference.setType(type);
        reference.setState(state);
        reference.setRegisteredDate(registeredDate);
        reference.setIssuedDate(issuedDate);
        reference.setPurpose(purpose);
        reference.setOwnerId(ownerId);

        return reference;
    }
}
