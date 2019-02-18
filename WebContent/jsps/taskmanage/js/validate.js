/**
 * 校验长度长度在min 与 max之间(包含min与max) 返回true
 * @param src
 * @param min
 * @param max
 */
function lengthValidate(src, min, max) {
	if (src.length <= max && src.length >= min) {
		return true;
	}
}