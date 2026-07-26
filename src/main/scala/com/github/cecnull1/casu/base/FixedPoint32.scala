package com.github.cecnull1.casu.base

class FixedPoint32 private (val px32x32: Long) extends AnyVal:
  def view: Long = px32x32
  def int: Long = px32x32 >> 32
  def point: Long = px32x32 & 0xFFFFFFFFL
  def +(r: FixedPoint32): FixedPoint32 = new FixedPoint32(this.px32x32 + r.px32x32)
  def -(r: FixedPoint32): FixedPoint32 = new FixedPoint32(this.px32x32 - r.px32x32)
  def *(r: FixedPoint32): FixedPoint32 =
    new FixedPoint32((this.int * r.int << 32) + (this.point * r.int + this.int * r.point) + (this.point * r.point >>> 32))

  def /(r: FixedPoint32): FixedPoint32 = {
    if r.px32x32 == 0L then throw new ArithmeticException("Division by zero")
    else {
      val a = this.px32x32
      val b = r.px32x32
      val sign = if (a < 0) == (b < 0) then -1L else 1L
      val absA = if a < 0 then -a else a
      val absB = if b < 0 then -b else b

      // 初始猜测
      var x = FixedPoint32(1.0 / (absB.toDouble / 4294967296.0))
      if x.px32x32 < 0 then x = FixedPoint32(-x.px32x32)

      // 手动展开 6 次牛顿迭代
      val two = FixedPoint32(2)
      val bFixed = FixedPoint32(absB)

      x = x * (two - bFixed * x)
      x = x * (two - bFixed * x)
      x = x * (two - bFixed * x)
      x = x * (two - bFixed * x)
      x = x * (two - bFixed * x)
      x = x * (two - bFixed * x)

      val result = FixedPoint32(absA) * x
      FixedPoint32(result.px32x32 * sign)
    }
  }
  def %(r: FixedPoint32): FixedPoint32 = new FixedPoint32(this.px32x32 % r.px32x32)

  def unary_- : FixedPoint32 = new FixedPoint32(-this.px32x32)
  def unary_+ : FixedPoint32 = new FixedPoint32(+this.px32x32)
  def unary_~ : FixedPoint32 = new FixedPoint32(~this.px32x32)

  def <<(r: FixedPoint32) = new FixedPoint32(this.px32x32 << r.px32x32)
  def >>>(r: FixedPoint32) = new FixedPoint32(this.px32x32 >>> r.px32x32)
  def >>(r: FixedPoint32) = new FixedPoint32(this.px32x32 >> r.px32x32)

  def ==(r: FixedPoint32): Boolean = this.px32x32 == r.px32x32
  def !=(r: FixedPoint32): Boolean = this.px32x32 != r.px32x32
  def <(r: FixedPoint32): Boolean = this.px32x32 < r.px32x32
  def <=(r: FixedPoint32): Boolean = this.px32x32 <= r.px32x32
  def >(r: FixedPoint32): Boolean = this.px32x32 > r.px32x32
  def >=(r: FixedPoint32): Boolean = this.px32x32 >= r.px32x32

  def |(r: FixedPoint32): FixedPoint32 = new FixedPoint32(this.px32x32 | this.px32x32)
  def &(r: FixedPoint32): FixedPoint32 = new FixedPoint32(this.px32x32 & r.px32x32)
  def ^(r: FixedPoint32): FixedPoint32 = new FixedPoint32(this.px32x32 ^ r.px32x32)

  def toHexString: String =
    val intPart = (px32x32 >>> 32).toInt
    val fracPart = (px32x32 & 0xFFFFFFFFL).toInt
    f"$intPart%08X.$fracPart%08X"
  end toHexString

  override def toString: String = toHexString

  def toDouble: Double = px32x32.toDouble / 4294967296.0
  def toFloat: Float = px32x32.toFloat / 4294967296.0f
end FixedPoint32

object FixedPoint32:
  private final val SCALE = 1L << 32
  def apply(long: Long): FixedPoint32 = new FixedPoint32(long)
  def apply(double: Double): FixedPoint32 =
    val scaled = double * 4294967296.0  // 2^32
    val rounded = Math.round(scaled)    // 四舍五入
    new FixedPoint32(rounded)
  end apply

  def fromHexString(hex: String): FixedPoint32 =
    val parts = hex.split('.')
    if (parts.length != 2) throw new IllegalArgumentException("Invalid hex format: expected 'XXXXXXXX.XXXXXXXX'")
    val intPart = java.lang.Long.parseLong(parts(0), 16)
    val fracPart = java.lang.Long.parseLong(parts(1), 16)
    new FixedPoint32((intPart << 32) | (fracPart & 0xFFFFFFFFL))
  end fromHexString
end FixedPoint32