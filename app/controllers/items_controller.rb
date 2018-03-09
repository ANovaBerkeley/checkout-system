class ItemsController < ApplicationController
  before_action :set_item, only: [:show, :edit, :update, :destroy]

  def index
    @items = Item.all
  end

  def new
    @item = Item.new
  end

  def show
    # @item = Item.first
    # TODO: Following line should find correct item by id
    @item = Item.find(params[:id])
  end

  def edit
  end

  def create
    params[:item][:remaining_quantity] = params[:item][:quantity]
    @item = Item.new(item_params)
    if @item.save
      redirect_to :root, notice: 'Item was successfully created.'
    else
      render :new
    end
  end

  def import
    begin
      Item.import(params[:file])
      redirect_to :root, notice: 'Items successfully created from CSV.'
    rescue
      redirect_to :root, notice: 'Invalid CSV format.'
    end
  end

  def update
    if @item.update(item_params)
      redirect_to items_url, notice: 'Item was successfully updated.'
    else
      render :edit
    end
  end

  def destroy
    @item.destroy
    redirect_to items_url, notice: 'Item was successfully destroyed.'
  end

  private
  def set_item
    @item = Item.find(params[:id])
  end

  def item_params
    params.require(:item).permit(:name, :category, :quantity, :description, :remaining_quantity, :upc)
  end
end
