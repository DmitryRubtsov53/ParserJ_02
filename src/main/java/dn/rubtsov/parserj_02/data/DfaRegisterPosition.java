package dn.rubtsov.parserj_02.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DfaRegisterPosition {
    private String productId;
    private String accountingDate;
    private Registers registers;
}
