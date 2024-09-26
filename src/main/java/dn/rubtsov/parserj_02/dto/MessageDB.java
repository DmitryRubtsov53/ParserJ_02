package dn.rubtsov.parserj_02.dto;

import dn.rubtsov.parserj_02.data.Header;
import dn.rubtsov.parserj_02.data.Registers;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDB {
    private Header header;
    private Registers registers;
}
