package dn.rubtsov.parserj_02.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
    private Header header;
    private DfaRegisterPosition dfaRegisterPosition;
}
